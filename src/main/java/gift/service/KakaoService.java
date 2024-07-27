package gift.service;

import gift.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoService.class);

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.api-key}")
    private String apiKey;

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.message-url}")
    private String messageUrl;

    private final RestTemplate kakaoRestTemplate;

    public KakaoService(RestTemplate kakaoRestTemplate) {
        this.kakaoRestTemplate = kakaoRestTemplate;
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

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response;
        try {
            response = kakaoRestTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            logger.info("Access token response: {}", response);
        } catch (Exception e) {
            logger.error("Failed to get access token", e);
            throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.", e);
        }

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.");
        }
    }

    public void sendKakaoMessage(OrderResponse order, String accessToken) {
        String message = String.format("User: %s\nProduct: %s\nQuantity: %d", order.getUserName(), order.getProductName(), order.getQuantity());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("message", message);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response;
        try {
            response = kakaoRestTemplate.postForEntity(messageUrl, request, String.class);
            logger.info("Kakao message response: {}", response);
        } catch (Exception e) {
            logger.error("Failed to send Kakao message", e);
            throw new RuntimeException("카카오 메시지 전송 중 예외가 발생했습니다.: " + e.getMessage(), e);
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 메시지 전송에 실패했습니다.: " + response.getBody());
        }
    }
}