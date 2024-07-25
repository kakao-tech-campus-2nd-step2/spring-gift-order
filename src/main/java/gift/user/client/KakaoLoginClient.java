package gift.user.client;

import gift.user.config.KakaoProperties;
import gift.user.dto.response.KakaoTokenResponse;
import gift.user.dto.response.KakaoUserInfoResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLoginClient {

    private final RestClient restClient;

    private final KakaoProperties properties;

    public KakaoLoginClient(RestClient restClient, KakaoProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public KakaoTokenResponse getKakaoTokenResponse(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", code);

        return restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(KakaoTokenResponse.class);
    }

    public KakaoUserInfoResponse getKakaoUserId(String token) {
        var url = "https://kapi.kakao.com/v2/user/me?property_keys=[]";
        return restClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(KakaoUserInfoResponse.class);
    }

}
