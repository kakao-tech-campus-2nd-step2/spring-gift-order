package gift.service;

import gift.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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

        ResponseEntity<Map> response = kakaoRestTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null) {
                return (String) responseBody.get("access_token");
            }
        }
        throw new RuntimeException("엑세스 토큰을 받을 수 없습니다.");
    }

    public void sendKakaoMessage(Order order) {
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

        ResponseEntity<String> response = kakaoRestTemplate.postForEntity(apiUrl, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 메시지를 받을 수 없습니다.: " + response.getBody());
        }
    }
}