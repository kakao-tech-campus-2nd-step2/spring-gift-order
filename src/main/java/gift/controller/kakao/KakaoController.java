package gift.controller.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
@EnableConfigurationProperties(KakaoProperties.class)
public class KakaoController {

    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KakaoProperties kakaoProperties;

    //access token 과 유저정보 매핑해서 저장하는 로직 추후 추가
    @PostMapping("/login")
    public Map<String, Object> kakaoLogin(@RequestParam("code") String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String clientId = kakaoProperties.clientId();
        String redirectUri = kakaoProperties.redirectUri();

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        try {
            var tokenResponse = requestAccessToken(tokenUrl, body);
            String accessToken = extractAccessToken(tokenResponse);

            Map<String, Object> userInfo = getUserInfo(accessToken);
            userInfo.put("access_token", accessToken);

            return userInfo;

        } catch (RestClientException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    private String requestAccessToken(String url, LinkedMultiValueMap<String, String> body) {
        try {
            var response = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
            return response;
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to request access token", e);
        }
    }

    private String extractAccessToken(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract access token", e);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        Map<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            String response = client.post()
                    .uri(URI.create(reqURL))
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            JsonNode jsonNode = objectMapper.readTree(response);

            // Extract user ID
            long id = jsonNode.path("id").asLong();

            // Extract properties (nickname)
            JsonNode properties = jsonNode.path("properties");
            String nickname = properties.path("nickname").asText();

            // Extract kakao_account (email)
            JsonNode kakaoAccount = jsonNode.path("kakao_account");
            String email = kakaoAccount.path("email").asText();

            // Add extracted information to userInfo map
            userInfo.put("id", id);
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to get user info", e);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get user info", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userInfo;
    }
}
