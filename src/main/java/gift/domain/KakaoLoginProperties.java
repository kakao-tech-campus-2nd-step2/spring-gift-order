package gift.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public class KakaoLoginProperties {
    private String clientId;
    private String redirectUrl;
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
