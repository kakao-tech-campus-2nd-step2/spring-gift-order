package gift.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoTokenProvider {
    private final KakaoProperties kakaoProperties;
    private final RestClient client = RestClient.builder().build();

    public KakaoTokenProvider(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getToken(String code) throws JsonProcessingException {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_url", kakaoProperties.getRedirectUrl());
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
