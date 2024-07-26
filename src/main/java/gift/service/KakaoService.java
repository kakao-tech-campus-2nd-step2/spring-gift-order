package gift.service;

import gift.dto.OrderResponse;
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

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.api-key}")
    private String apiKey;

    @Value("${kakao.api-url}")
    private String apiUrl;

    private final KakaoProperties kakaoProperties;
    private final RestTemplate kakaoRestTemplate;

    public KakaoService(RestTemplate kakaoRestTemplate, KakaoProperties kakaoProperties) {
        this.kakaoRestTemplate = kakaoRestTemplate;
        this.kakaoProperties = kakaoProperties;
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

        ResponseEntity<Map> response = null;
        try {
            response = kakaoRestTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.", e);
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                return (String) responseBody.get("access_token");
            }
        }
        throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.");
    }

    public void sendKakaoMessage(OrderResponse order) {
        String message = String.format("Order ID: %d\nQuantity: %d\nMessage: %s", order.getId(), order.getQuantity(), order.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", Map.of("web_url", "http://example.com/orders/" + order.getId()));
        templateObject.put("button_title", "View Order");

        Map<String, Object> body = new HashMap<>();
        body.put("template_object", templateObject);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = null;
        try {
            response = kakaoRestTemplate.postForEntity(apiUrl, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 메시지 전송 중 예외가 발생했습니다.: " + e.getMessage(), e);
        }

        if (response == null) {
            throw new RuntimeException("카카오 메시지 전송에 실패했습니다.: null response");
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 메시지 전송에 실패했습니다.: " + response.getBody());
        }
    }
}