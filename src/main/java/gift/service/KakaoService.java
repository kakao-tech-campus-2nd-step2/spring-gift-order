package gift.service;

import gift.domain.KakaoToken;
import gift.domain.Order;
import gift.domain.OrderItem;
import gift.repository.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    private KakaoTokenRepository kakaoTokenRepository;

    public KakaoService(KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public String getAccessToken(String authorizationCode) throws Exception {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            } else {
                throw new Exception("Failed to get access token from Kakao");
            }
        } catch (RestClientException e) {
            throw new Exception("Error while requesting access token from Kakao", e);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) throws Exception {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new Exception("Failed to get user info from Kakao");
            }
        } catch (RestClientException e) {
            throw new Exception("Error while requesting user info from Kakao", e);
        }
    }

    public void sendOrderMessage(Long memberId, Order order) throws Exception {
        KakaoToken kakaoToken = kakaoTokenRepository.findByMemberId(memberId);
        if (kakaoToken == null) {
            throw new Exception("Kakao token not found for member ID " + memberId);
        }

        String message = createOrderMessage(order);
        sendMessage(kakaoToken.getAccessToken(), message);
    }

    private String createOrderMessage(Order order) {
        // 사용자 정의 템플릿을 사용하여 메시지 작성
        StringBuilder message = new StringBuilder();
        message.append("주문 내역:\n");
        for (OrderItem item : order.getOrderItems()) {
            message.append(item.getOption().getName()).append(" - ").append(item.getQuantity()).append("\n");
        }
        message.append("수령인 메시지: ").append(order.getRecipientMessage());
        return message.toString();
    }

    private void sendMessage(String accessToken, String message) throws Exception {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", "{\"object_type\":\"text\",\"text\":\"" + message + "\",\"link\":{\"web_url\":\"http://localhost:8080\"}}");

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new Exception("Failed to send Kakao message");
            }
        } catch (RestClientException e) {
            throw new Exception("Error while sending Kakao message", e);
        }
    }
}
