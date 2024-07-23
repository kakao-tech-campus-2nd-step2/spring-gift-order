package gift.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ConfigurationProperties(prefix = "kakao")
record KakaoProperties(
        String clientId,
        String redirectUri
) {
}

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties(KakaoProperties.class)
public class RestClientTest {
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KakaoProperties kakaoProperties;

    @Test
    void testKakaoLoginFlow() {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String clientId = kakaoProperties.clientId();
        String redirectUri = kakaoProperties.redirectUri();
        String authorizationCode = "WOjLU2TAdWQgwcXYt4AgU3iKxn4X94lc52MdXxzv6bMQLkeTCjOjMwAAAAQKKiUPAAABkN8G-KjHP8VuE1ZNOQ";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        try {
            var tokenResponse = requestAccessToken(tokenUrl, body);
            assertThat(tokenResponse).isNotNull();
            System.out.println(tokenResponse);

            String accessToken = extractAccessToken(tokenResponse);

            var userInfo = getUserInfo(accessToken);
            System.out.println(userInfo);

        } catch (RestClientException e) {
            System.err.println("Request failed: " + e.getMessage());
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
            System.err.println("Error response from server: " + e.getResponseBodyAsString());
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

    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            String response = client.post()
                    .uri(URI.create(reqURL))
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            System.out.println("User info response: " + response);

            JsonNode jsonNode = objectMapper.readTree(response);

            JsonNode properties = jsonNode.path("properties");
            JsonNode kakaoAccount = jsonNode.path("kakao_account");

            userInfo.put("nickname", properties.path("nickname").asText("Unknown"));
            userInfo.put("email", kakaoAccount.path("email").asText("Unknown"));

        } catch (RestClientResponseException e) {
            System.err.println("Error response from server: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to get user info", e);
        } catch (RestClientException e) {
            System.err.println("Error occurred: " + e.getMessage());
            throw new RuntimeException("Failed to get user info", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userInfo;
    }
}
