package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUri;
    private final String contentType;
    private final String grantType;
    private final String userInfoUrl;
    private final String tokenUrl;
    private final String responseType;

    @ConstructorBinding
    public KakaoProperties(String clientId, String redirectUri, String contentType,
        String grantType, String userInfoUrl, String tokenUrl, String responseType) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.contentType = contentType;
        this.grantType = grantType;
        this.userInfoUrl = userInfoUrl;
        this.tokenUrl = tokenUrl;
        this.responseType = responseType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getContentType() {
        return contentType;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getResponseType() {
        return responseType;
    }
}
