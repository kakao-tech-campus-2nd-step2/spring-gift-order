package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProperties;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class OAuthController {
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OAuthController(KakaoProperties kakaoProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<String> getAccessToken(@RequestParam("code") String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        try {
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String scope = responseBody.get("scope") != null ? responseBody.get("scope").asText() : "";
            if (!scope.contains("talk_message")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.");
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류: " + e.getMessage());
        }
        return response;
    }
}