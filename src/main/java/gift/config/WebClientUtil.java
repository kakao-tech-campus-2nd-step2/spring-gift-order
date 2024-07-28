package gift.config;

import org.springframework.web.reactive.function.client.WebClient;


public class WebClientUtil {
    private final WebClient.Builder webClientBuilder;

    public WebClientUtil(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public WebClient createWebClient(String baseUrl) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }
}
