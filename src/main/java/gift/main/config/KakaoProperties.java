package gift.main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kakao")
public class KakaoProperties {

    private String grantType = "authorization_code";
    private String codeRequestUri;
    private String tokenRequestUri;
    private String UserRequestUri;
    private String RedirectUri;
    private String clientId;
    private String Password;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getCodeRequestUri() {
        return codeRequestUri;
    }

    public void setCodeRequestUri(String codeRequestUri) {
        this.codeRequestUri = codeRequestUri;
    }

    public String getTokenRequestUri() {
        return tokenRequestUri;
    }

    public void setTokenRequestUri(String tokenRequestUri) {
        this.tokenRequestUri = tokenRequestUri;
    }

    public String getRedirectUri() {
        return RedirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        RedirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserRequestUri() {
        return UserRequestUri;
    }

    public void setUserRequestUri(String userRequestUri) {
        UserRequestUri = userRequestUri;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

