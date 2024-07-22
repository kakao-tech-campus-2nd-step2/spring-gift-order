package gift.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("kakao")
public class KakaoProperties {

    private final String clientId;

    @ConstructorBinding
    public KakaoProperties(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

}
