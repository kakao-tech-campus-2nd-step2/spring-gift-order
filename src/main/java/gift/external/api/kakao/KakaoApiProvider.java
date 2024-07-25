package gift.external.api.kakao;

import gift.external.api.kakao.client.KakaoApiClient;
import gift.external.api.kakao.client.KakaoAuthClient;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoApiProvider {

    private final KakaoProperties kakaoProperties;
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    private static final String[] SCOPE = { "profile_nickname", "talk_message", "account_email" };
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    public KakaoApiProvider(
        KakaoProperties kakaoProperties,
        KakaoAuthClient kakaoAuthClient,
        KakaoApiClient kakaoApiClient
    ) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
    }

    public String getAuthCodeUrl() {
        return kakaoProperties.authBaseUrl()
            + "?scope=" + String.join(",", SCOPE)
            + "&response_type=" + RESPONSE_TYPE
            + "&redirect_uri=" + kakaoProperties.redirectUri();
    }

    public KakaoToken getToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        return kakaoAuthClient.getAccessToken(body);
    }

    public void validateAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        kakaoApiClient.getAccessTokenInfo(headers);
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return kakaoApiClient.getUserInfo(headers);
    }
}
