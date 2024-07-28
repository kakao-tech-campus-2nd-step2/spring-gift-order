package gift.service;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

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
        return UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .build()
                .toUriString();
    }
}
