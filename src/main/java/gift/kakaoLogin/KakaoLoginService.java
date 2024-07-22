package gift.kakaoLogin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoLoginService {

    @Value("${kakao.client-id}")
    private String REST_API_KEY;
    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;
    private final RestClient client = RestClient.create();

    public String login(String code){
        var url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = createBody(code);

        ResponseEntity<Map> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(status -> status.value() != 200, ((request, response1) -> {
                    throw new RuntimeException("kakao " + response1.getStatusCode());
                }))
                .toEntity(Map.class);

        if(response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            throw new IllegalArgumentException();
        }

        return (String) response.getBody().get("access_token");
    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_url", REDIRECT_URI);
        body.add("code", code);
        return body;
    }

}
