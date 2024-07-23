package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUri;
    private final String authUrl;
    private final String tokenUrl;
    private final String infoUrl;

    @ConstructorBinding
    public KakaoProperties(String clientId, String redirectUri, String authUrl, String tokenUrl, String infoUrl) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.authUrl = authUrl;
        this.tokenUrl = tokenUrl;
        this.infoUrl = infoUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }
}
