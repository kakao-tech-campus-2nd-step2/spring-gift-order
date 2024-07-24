package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Kakao.*;
import gift.DTO.Product.ProductResponse;
import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.domain.User;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class KakaoUserService {
    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${kakao.loginUrl}")
    private String loginUrl;
    @Value("${kakao.messageUrl}")
    private String messageUrl;

    public KakaoUserService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            KakaoProperties kakaoProperties
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoProperties = kakaoProperties;
    }
    /*
     * Code를 이용하여 트큰을 발급하는 로직
     */
    @Transactional
    public Token login(String code) throws JsonProcessingException {
        String token = kakaoTokenProvider.getToken(code);
        User kakaoUser = kakaoTokenProvider.getKakaoUserInfo(token);

        return jwtTokenProvider.makeToken(new UserRequest(
                kakaoUser.getUserId(), kakaoUser.getEmail(), kakaoUser.getPassword()
        ));
    }
    /*
     * 상품 구매에 따른 동작 중 "나에게 메세지 보내기" 로직
     */
    public void messageToMe(
            String accessToken, ProductResponse productResponse, String optionName, String message
    ) throws JsonProcessingException {
        LinkedMultiValueMap<Object, Object> body = makeBody(productResponse, optionName, message);
        client.post()
                .uri(URI.create(messageUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);
    }
    /*
     * meesage Api 호출을 위한 요청의 body를 만들어주는 로직
     */
    private static LinkedMultiValueMap<Object, Object> makeBody(
            ProductResponse productResponse, String optionName, String message
    ) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Link link = new Link("localhost:8080");
        Content content = new Content(
                productResponse.getName() + "\n" + optionName + "\n" + message,
                productResponse.getImageUrl(),
                "",
                link
        );
        Commerce commerce = new Commerce(productResponse.getName(), productResponse.getPrice());
        Template template = new Template("commerce", content, commerce);

        String template_str = objectMapper.writeValueAsString(template);

        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.set("template_object", template_str);
        return body;
    }
    /*
     * login을 위해 code를 받기 위한 url을 만드는 로직
     */
    public String makeLoginUrl(){
        loginUrl += "?scope=talk_message";
        loginUrl += "&response_type=code";
        loginUrl += "&redirect_uri=" + kakaoProperties.getRedirectUrl();
        loginUrl += "&client_id=" + kakaoProperties.getClientId();
        return loginUrl;
    }
}
