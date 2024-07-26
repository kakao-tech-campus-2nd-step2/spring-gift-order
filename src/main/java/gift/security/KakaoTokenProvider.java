package gift.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoTokenProvider {

    private final RestClient client = RestClient.builder().build();

    public String getToken(String code) throws Exception {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "e8737f91343ed26716bff4973870e1b9");  // 하드코딩된 클라이언트 아이디
        body.add("redirect_uri", "http://localhost:8080");  // 하드코딩된 리디렉트 URI
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

        return jsonNode.get("access_token").asText();
    }
}