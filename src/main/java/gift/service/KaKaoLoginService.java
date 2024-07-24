package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.net.URI;

@Service
public class KaKaoLoginService {

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    @Value("${kakao.grant-type}")
    private String grantType;

    @Value("${kakao.get-token.url}")
    private String getTokenUrl;

    public void getAccessToken(String code) {
        RestClient client = RestClient.builder().build();

        LinkedMultiValueMap<String, String> body = createBody(code);

        var response = client.post()
                .uri(URI.create(getTokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        System.out.println(response);
    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_url",redirectUrl);
        body.add("code", code);
        return body;
    }
}
