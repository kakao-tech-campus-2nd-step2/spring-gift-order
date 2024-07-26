package gift.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {

    private final WebClient webClient;

    public KakaoMessageServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }

    @Override
    public void sendMessage(String kakaoToken, String message) {
        String templateObject = "{\"object_type\":\"text\",\"text\":\"" + message + "\"}";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        try {
            webClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header("Authorization", "Bearer " + kakaoToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Kakao message: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
        }
    }
}
