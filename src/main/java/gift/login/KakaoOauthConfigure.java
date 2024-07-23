package gift.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoOauthConfigure {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectURL;

    @Value("${kakao.authorize-code-url}")
    private String authorizeCodeURL;

    @Value("${kakao.token-url}")
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
