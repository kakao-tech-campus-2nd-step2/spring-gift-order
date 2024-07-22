package gift.service.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Service
public class KakaoLoginService {

    private final KakaoProperties properties;
    private final RestClient client;

    public KakaoLoginService(KakaoProperties properties) {
        this.properties = properties;
        this.client = RestClient.create();
    }

    public HttpHeaders getRedirectHeaders() {
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
                + properties.redirectUrl()
                + "&client_id=" + properties.clientId();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return headers;
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";

        var headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        KakaoTokenInfoResponse tokenResponse = client.post()
                .uri(URI.create(url))
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(KakaoTokenInfoResponse.class);

        return tokenResponse.getAccessToken();
    }

}
