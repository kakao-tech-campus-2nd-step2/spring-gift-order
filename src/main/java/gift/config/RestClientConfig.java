package gift.config;

import gift.auth.oauth.kakao.KakaoProperties;
import gift.client.KakaoApiClient;
import gift.client.KakaoAuthClient;
import gift.exception.OauthLoginException;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@ConfigurationPropertiesScan
public class RestClientConfig {

    private final KakaoProperties kakaoProperties;

    public RestClientConfig(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @Bean
    public KakaoApiClient kakaoApiClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
            .baseUrl(kakaoProperties.apiBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new OauthLoginException("error.oauth.response");
            })
            .build();
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build().createClient(KakaoApiClient.class);
    }

    @Bean
    public KakaoAuthClient kakaoAuthClient(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
            .baseUrl(kakaoProperties.authBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new OauthLoginException("error.oauth.response");
            })
            .build();
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build().createClient(KakaoAuthClient.class);
    }
}
