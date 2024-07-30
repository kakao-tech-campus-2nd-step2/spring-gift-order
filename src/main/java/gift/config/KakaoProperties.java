package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private String clientId;
    private String redirectUri;
    private String accessTokenInfoUrl;
    private String messageUrl;
    private String authUrl;
    private String apiKey;
    private String apiUrl;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public String getAccessTokenInfoUrl() {
        return accessTokenInfoUrl;
    }
}