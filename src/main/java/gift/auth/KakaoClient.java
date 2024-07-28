package gift.auth;

import java.net.URI;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final static String KAKAO_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;

    public KakaoClient(KakaoProperties kakaoProperties) {
        this.restClient = RestClient.builder().build();
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = kakaoProperties.createBody(authorizationCode);

        var response = restClient.post()
            .uri(URI.create(KAKAO_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessToken.class);

        return Objects.requireNonNull(response.getBody()).accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return restClient.get()
            .uri(KAKAO_USER_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(KakaoUserInfo.class)
            .getBody();
    }

}
