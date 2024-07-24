package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthService(RestTemplate restTemplate, ObjectMapper objectMapper, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.kakaoProperties = kakaoProperties;
    }

    public ResponseEntity<String> postForEntity(URI uri, HttpHeaders headers, MultiValueMap<String, String> body) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
    }

    public KakaoTokenResponse getAccessToken(String code) {
        try {
            String tokenUri = "https://kauth.kakao.com/oauth/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", kakaoProperties.getClientId());
            body.add("redirect_uri", kakaoProperties.getRedirectUri());
            body.add("code", code);
            body.add("client_secret", kakaoProperties.getClientSecret());

            ResponseEntity<String> response = postForEntity(new URI(tokenUri), headers, body);
            return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    public String getAuthorizationUri() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code" +
                "&client_id=" + kakaoProperties.getClientId() +
                "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }
}
