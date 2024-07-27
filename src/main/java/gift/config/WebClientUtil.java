package gift.config;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

public class WebClientUtil {
    private final WebClient.Builder webClientBuilder;

    public WebClientUtil(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public WebClient createWebClient(String baseUrl) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }

    public void handleErrorResponses(WebClient.ResponseSpec responseSpec) {
        responseSpec.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")));
    }
}
