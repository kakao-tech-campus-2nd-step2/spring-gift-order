package gift.service;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthService(KakaoApiClient kakaoApiClient, KakaoProperties kakaoProperties) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        return kakaoApiClient.getAccessToken(code);
    }

    public String getAuthorizationUri() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code" +
                "&client_id=" + kakaoProperties.getClientId() +
                "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }
}
