package gift.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUrl;
    private final String authorizeUrl;
    private final String tokenUrl;
    private final String tokenInfoUrl;
    private final String userInfoUrl;

    public KakaoProperties(String clientId, String redirectUrl, String authorizeUrl,
        String tokenUrl,
        String tokenInfoUrl, String userInfoUrl) {
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        this.authorizeUrl = authorizeUrl;
        this.tokenUrl = tokenUrl;
        this.tokenInfoUrl = tokenInfoUrl;
        this.userInfoUrl = userInfoUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getTokenInfoUrl() {
        return tokenInfoUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }
}