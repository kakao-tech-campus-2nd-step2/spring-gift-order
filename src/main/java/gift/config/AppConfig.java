package gift.config;

import gift.exception.ApiRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate kakaoRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setErrorHandler(errorHandler());
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 연결 타임아웃 5초
        factory.setReadTimeout(5000);     // 읽기 타임아웃 5초
        return factory;
    }

    private ResponseErrorHandler errorHandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    throw new ApiRequestException("Bad Request");
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new ApiRequestException("Not Found");
                } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    throw new ApiRequestException("Internal Server Error");
                } else {
                    super.handleError(response);
                }
            }
        };
    }
}