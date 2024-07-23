package gift.auth.oauth.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final KakaoProperties kakaoProperties;

    private static final RestClient restClient = RestClient.create();
    private static final String REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String GRANT_TYPE = "authorization_code";

    public KakaoLoginService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        JsonNode jsonResponse = restClient.post()
            .uri(URI.create(REQUEST_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(JsonNode.class);

        return jsonResponse.get("access_token").asText();
    }
}
