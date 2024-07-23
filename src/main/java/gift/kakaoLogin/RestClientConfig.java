package gift.kakaoLogin;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withReadTimeout(Duration.ofMinutes(2)).withConnectTimeout(Duration.ofMinutes(2));
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactories.get(settings);

        return RestClient.builder()
                .requestFactory(requestFactory)
            .defaultHeaders(headers -> {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        });
    }

    @Bean
    public RestClient kakaoRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder.build();
    }
}
