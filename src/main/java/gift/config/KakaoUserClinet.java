package gift.config;

import gift.model.member.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoUserClinet {
    private final WebClient webClient;

    @Autowired
    public KakaoUserClinet(WebClient.Builder webClientBuilder, KakaoProperties kakaoProperties) {
        this.webClient = webClientBuilder
                .baseUrl(kakaoProperties.getUserInfoUrl())
                .build();
    }

    public Mono<Integer> sendOrderConfirmationMessage(String message, String accessToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/api/talk/memo/default/send")
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("template_object=" + message)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(Integer.class);
    }
}
