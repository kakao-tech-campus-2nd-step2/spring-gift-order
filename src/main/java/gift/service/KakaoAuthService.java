package gift.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoAuthService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate kakaoRestTemplate;

    public KakaoAuthService(RestTemplate kakaoRestTemplate) {
        this.kakaoRestTemplate = kakaoRestTemplate;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);
        body.add("scope", "talk_message");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response;
        try {
            logger.info("Requesting access token with code: {}", code);
            response = kakaoRestTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            logger.info("Access token response: {}", response);
        } catch (Exception e) {
            logger.error("Failed to get access token", e);
            throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.", e);
        }

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String accessToken = (String) response.getBody().get("access_token");
            logger.info("Received access token: {}", accessToken);
            return accessToken;
        } else {
            throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.");
        }
    }

    public void validateAccessToken(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response;
        try {
            logger.info("Validating access token: {}", accessToken);
            response = kakaoRestTemplate.exchange(url, HttpMethod.GET, request, String.class);
            logger.info("Access token info: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Failed to validate access token", e);
            logger.error("HTTP Status: {}", e.getStatusCode());
            logger.error("Response Body: {}", e.getResponseBodyAsString());
            throw new RuntimeException("엑세스 토큰 검증 중 오류가 발생했습니다.: " + e.getMessage(), e);
        }
    }
}