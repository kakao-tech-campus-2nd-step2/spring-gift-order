package gift.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import gift.common.config.KakaoProperties;
import gift.order.domain.Token;
import gift.order.dto.KakaoUser;
import gift.order.dto.KakaoTokenResponse;
import gift.order.repository.TokenJPARepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;

@Service
public class KakaoService {
    private final KakaoProperties kakaoProperties;
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenJPARepository tokenJPARepository;
    public KakaoService(KakaoProperties kakaoProperties, TokenJPARepository tokenJPARepository) {
        this.kakaoProperties = kakaoProperties;
        this.tokenJPARepository = tokenJPARepository;
    }

    // accessToken과 refreshToken을 받아와 저장하기
    @Transactional
    public ResponseEntity<KakaoTokenResponse> getToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        // body
        LinkedMultiValueMap<String, String> body = createBody(code);

        // response
        KakaoTokenResponse kakaoTokenResponse = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();

        return ResponseEntity.ok(kakaoTokenResponse);
    }

    // RestClient의 body를 만들기
    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoProperties.grantType()); // authorization_code로 고정
        body.add("client_id", kakaoProperties.clientId()); // REST API 키
        body.add("redirect_uri", kakaoProperties.redirectUri()); // 인가 코드가 리다이렉트된 URI
        body.add("code", code); // 인가 코드

        return body;
    }

    // accesstoken을 활용하여 사용자 정보 받아와 저장하기
    @Transactional
    public KakaoUser getUserInfo(String accessToken) throws IOException {
        String url = "https://kapi.kakao.com/v2/user/me";

        // response
        String response = client.get()
                .uri(URI.create(url))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        // KakaoMember
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode kakaoAccount = jsonNode.get("kakao_account");
        Long id = jsonNode.get("id").asLong();
        String nickname = kakaoAccount.get("profile").get("nickname").asText();

        return new KakaoUser(id, nickname);
    }

    // 토큰값과 사용자 정보 저장하기
    @Transactional
    public void saveToken(KakaoTokenResponse tokenResponse, KakaoUser kakaoUser) {
        String accessToken = tokenResponse.accessToken();
        String refreshToken = tokenResponse.refreshToken();
        String userName = kakaoUser.nickname();
        Token token = new Token(accessToken, refreshToken, userName);

        tokenJPARepository.save(token);
    }
}
