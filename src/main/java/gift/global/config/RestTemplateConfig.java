package gift.global.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(5)) // 연결
            .setReadTimeout(Duration.ofSeconds(5)) // 읽기
            .additionalInterceptors(clientHttpRequestInterceptor())
            .build();
    }

    /**
     *  RestTemplate 이 HTTP 요청 보낼때마다 요청 가로챔
     */
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return ((request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3)); // 최대 3번 재시도
            try {
                return retryTemplate.execute(context -> execution.execute(request, body)); // HTTP 요청 실행
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }


}
