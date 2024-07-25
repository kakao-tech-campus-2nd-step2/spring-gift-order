package gift.controller.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.json.Json;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String authCode) {
        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", authCode);

        String tokenResponse = restClient.post()
                                        .uri(tokenRequestUrl)
                                        .body(body)
                                        .retrieve()
                                        .body(String.class);
        String accessToken = extractAccessToken(tokenResponse);

        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }

    private String extractAccessToken(String tokenResponse) {
        try {
            JsonNode jsonNode = objectMapper.readTree(tokenResponse);
            return jsonNode.path("access_token").asText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }
}
