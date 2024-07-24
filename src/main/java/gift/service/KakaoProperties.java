package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProperties {

    @Value("31564f3826ea40870a9d8e4de532d4e1\n")
    private String clientId;

    @Value("${http://localhost:8080/\n}")
    private String redirectUri;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}