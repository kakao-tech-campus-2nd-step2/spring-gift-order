package gift.product.service;

import org.springframework.stereotype.Service;

@Service
public class KakaoService {

    private final KakaoProperties properties;

    public KakaoService(KakaoProperties properties) {
        this.properties = properties;
    }

    public String getAuthCode() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
            + "&redirect_uri=" + properties.redirectUrl()
            + "&client_id=" + properties.clientId();
    }
}