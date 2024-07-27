package gift.login;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoOauthConfigure {

    private String clientId;

    private String redirectURL;

    private String authorizeCodeURL;

    private String tokenURL;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getAuthorizeCodeURL() {
        return authorizeCodeURL;
    }

    public void setAuthorizeCodeURL(String authorizeCodeURL) {
        this.authorizeCodeURL = authorizeCodeURL;
    }

    public String getTokenURL() {
        return tokenURL;
    }

    public void setTokenURL(String tokenURL) {
        this.tokenURL = tokenURL;
    }
}
