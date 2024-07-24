package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private String redirectUri;
    private String restAPiKey;
    private String tokenUrl;
    private String authUrl;
    private String userInfoUrl;
    private String sendMessageUrl;

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getRestAPiKey() {
        return restAPiKey;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setRestAPiKey(String restAPiKey) {
        this.restAPiKey = restAPiKey;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public void setUserInfoUrl(String userInfoUrl) {
        this.userInfoUrl = userInfoUrl;
    }

    public void setSendMessageUrl(String sendMessageUrl) {
        this.sendMessageUrl = sendMessageUrl;
    }
}
