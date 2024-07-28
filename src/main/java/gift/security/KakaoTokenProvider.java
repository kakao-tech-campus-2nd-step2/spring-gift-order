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

import java.net.URI;

@Component
public class KakaoTokenProvider {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestClient client = RestClient.builder().build();

    // 새로운 액세스 토큰을 발급받는 메소드
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
            throw new RuntimeException("액세스 토큰 발급 실패: " + jsonNode.get("error_description").asText());
        }

        return jsonNode.get("access_token").asText();
    }

    // 카카오 메시지 전송
    public void sendMessage(String accessToken, String templateObject) throws Exception {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObject);

        ResponseEntity<String> entity = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (entity.getStatusCode().isError()) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + entity.getBody());
        }
    }
}