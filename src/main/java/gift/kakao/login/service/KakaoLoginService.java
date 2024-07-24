package gift.kakao.login.service;

import gift.kakao.login.dto.KakaoTokenResponseDTO;
import gift.kakao.login.dto.KakaoUser;
import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.user.repository.UserRepository;
import gift.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {
    private final RestClient client;

    private final String clientId;
    private final String redirectUri;

    private final UserRepository userRepository;
    private final UserService userService;

    public KakaoLoginService(@Value("${kakao.client-id}") String clientId,
        @Value("${kakao.redirect-uri}") String redirectUri,
        UserRepository userRepository,
        UserService userService){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.userRepository = userRepository;
        this.userService = userService;
        client = RestClient.builder().build();
    }

    public String getAccessToken(String code){
        var uri = "https://kauth.kakao.com/oauth/token";
        final var body = createAccessTokenBody(code);
        var response =  client.post()
            .uri(URI.create(uri))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoTokenResponseDTO.class);
        return response.getBody().getAccessToken();
    }
    public String getUserInfo(String accessToken){
        var uri = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("property_keys", "[\"kakao_account.email\"]");

        var response = client.get()
            .uri(URI.create(uri))
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .retrieve()
            .toEntity(KakaoUserInfoResponse.class);
        String email = response.getBody().getId();
        String password = "";
        if(userRepository.findByEmail(email).isEmpty()){
            userRepository.save(new KakaoUser(email, password, accessToken));
            return email;
        }
        KakaoUser kakaoUser = (KakaoUser) userRepository.findByEmail(email).get();
        kakaoUser.setToken(accessToken);
        userRepository.save(kakaoUser);
        return email;
    }

    private @NotNull LinkedMultiValueMap<String, String> createAccessTokenBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

}
