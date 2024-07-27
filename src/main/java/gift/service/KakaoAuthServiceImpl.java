package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoAuthServiceImpl implements KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";

    @Override
    public String getAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(tokenUrl));
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }
}
