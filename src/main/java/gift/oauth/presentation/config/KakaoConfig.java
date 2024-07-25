package gift.oauth.presentation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.oauth.kakao")
public class KakaoConfig {
    private final String clientId;
    private final String redirectUri;
    private final String grantType;

    public KakaoConfig(String clientId, String redirectUri, String grantType) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getGrantType() {
        return grantType;
    }
}
