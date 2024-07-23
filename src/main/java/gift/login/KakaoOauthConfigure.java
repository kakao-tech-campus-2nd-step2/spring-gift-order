package gift.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoOauthConfigure {

    @Value("${kakao.cliend-id}")
    private String cliendId;

    @Value("${kakao.redirect-url}")
    private String redirectURL;

    @Value("${kakao.authorize-code-url}")
    private String authorizeCodeURL;

    @Value("${kakao.token-url}")
    private String tokenURL;

    public String getCliendId() {
        return cliendId;
    }

    public void setCliendId(String cliendId) {
        this.cliendId = cliendId;
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
