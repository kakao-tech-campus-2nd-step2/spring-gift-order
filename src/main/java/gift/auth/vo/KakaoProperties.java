package gift.auth.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("kakao")
public class KakaoProperties {

    private final String grantType;
    private final String redirectUri;
    private final String clientId;

    @ConstructorBinding
    public KakaoProperties(String grantType,
                           String redirectUri,
                           String clientId) {
        this.grantType = grantType;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

}
