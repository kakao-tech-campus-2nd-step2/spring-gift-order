package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean(name = "customRestClient")
    public RestClient restClientBuilder() {
        //Timeout 설정
        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);

        return RestClient.builder().requestFactory(factory).build();
    }


}
