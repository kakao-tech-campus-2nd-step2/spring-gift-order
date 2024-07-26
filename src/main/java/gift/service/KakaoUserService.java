package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoProperties;
import gift.domain.OrderDTO;
import gift.exception.KakaoOAuthException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class KakaoUserService {
    private final KakaoProperties properties;
    private final RestClient client;

    public KakaoUserService(KakaoProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder().build();
    }

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
            + properties.redirectUrl() + "&client_id=" + properties.clientId();
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        try {
            var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        try {
            var response = client.get()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(Map.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }

    public void sendOrderMessage(String accessToken, OrderDTO order) throws JsonProcessingException {
        String templateObjectJson = createTemplate(order);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        try {
            client.post()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        } catch (HttpStatusCodeException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }

    private String createTemplate(OrderDTO order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 주문 정보 포함
        String orderDetails = String.format("옵션 id: %s, 수량: %d, 메시지: %s",
            order.getOptionId(), order.getQuantity(), order.getMessage());

        Map<String, Object> content = new HashMap<>();
        content.put("title", "주문해 주셔서 감사합니다.");
        content.put("description", "주문 내용: " + order.getMessage());
        content.put("image_url", "http://example.com/image.jpg");
        content.put("image_width", 640);
        content.put("image_height", 640);

        Map<String, Object> message = new HashMap<>();
        message.put("object_type", "text");
        message.put("text", "주문이 완료되었습니다.\n주문 내용: " + orderDetails);
        message.put("link", Map.of(
            "web_url", "www.naver.com",
            "mobile_web_url", "www.google.com"
        ));

        return objectMapper.writeValueAsString(message);
    }


}