package gift.model.member;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private final String clientId;
    private final String redirectUri;

    private final String kakaoAuthUrl;

    @ConstructorBinding
    public KakaoProperties(String clientId, String redirectUri, String kakaoAuthUrl) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.kakaoAuthUrl = kakaoAuthUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getKakaoAuthUrl() {
        return kakaoAuthUrl;
    }
}
