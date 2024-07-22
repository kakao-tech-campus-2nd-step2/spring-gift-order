package gift.service.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class KakaoLoginService {

    private final KakaoProperties properties;

    public KakaoLoginService(KakaoProperties properties) {
        this.properties = properties;
    }

    public HttpHeaders getRedirectHeaders() {
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
                + properties.redirectUrl()
                + "&client_id=" + properties.clientId();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return headers;
    }

}
