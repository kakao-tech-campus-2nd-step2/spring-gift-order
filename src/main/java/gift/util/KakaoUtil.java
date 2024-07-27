package gift.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class KakaoUtil {

    private final RestClient restClient;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    public KakaoUtil(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, Object> getAccessToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        String body = "grant_type=authorization_code" +
                "&client_id=" + kakaoClientId +
                "&redirect_uri=" + redirectUri +
                "&code=" + authorizationCode;

        return restClient
                .post()
                .uri(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(Map.class);
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        return restClient
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);
    }
}
