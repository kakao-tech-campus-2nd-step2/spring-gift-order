package gift.service.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoLoginService {

    private final KakaoProperties properties;
    private final RestTemplate restTemplate;

    public KakaoLoginService(KakaoProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public HttpHeaders getRedirectHeaders() {
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return headers;
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        KakaoTokenInfoResponse tokenResponse = restTemplate.postForObject(url, body, KakaoTokenInfoResponse.class);

        return tokenResponse.getAccessToken();
    }

}
