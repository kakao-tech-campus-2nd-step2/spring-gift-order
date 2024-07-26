package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientBuilderConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        System.out.println("??");
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(2000));
        factory.setReadTimeout(Duration.ofMillis(2000));

        return factory;
    }

//    @Bean
//    public RestClient restClient(RestClient.Builder builder) {
//        return builder.build();
//    }
//
//    @Bean
//    public RestClientCustomizer restClientCustomizer() {
//        return (restClientBuilder) -> restClientBuilder
//                .requestFactory(getClientHttpRequestFactory());
//    }
}
