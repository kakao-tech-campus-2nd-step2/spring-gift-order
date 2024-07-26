package gift.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;

@Component
public class KakaoTokenProvider {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestClient client = RestClient.builder().build();

    public String getToken(String code) throws Exception {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        ResponseEntity<String> entity = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);

        if (jsonNode.has("error")) {
            throw new RuntimeException("Failed to get access token: " + jsonNode.get("error_description").asText());
        }

        return jsonNode.get("access_token").asText();
    }

    public void sendMessage(String accessToken, String templateObject) throws Exception {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObject);

        try {
            ResponseEntity<String> entity = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);

            if (entity.getStatusCode().isError()) {
                throw new RuntimeException("Failed to send Kakao message: " + entity.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Failed to send Kakao message: " + e.getResponseBodyAsString(), e);
        }
    }

    public String getTokenForUser(String email) {
        return "sample_access_token";
    }
}