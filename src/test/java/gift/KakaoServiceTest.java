package gift;

import gift.exception.KakaoServiceException;
import gift.service.KakaoService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = KakaoServiceTest.TestConfig.class)
public class KakaoServiceTest {


    @Autowired
    private KakaoService kakaoService;

    @Autowired
    @Qualifier("kakaoWebClient")
    private WebClient webClient;

    private MockWebServer mockWebServer;

    @Configuration
    static class TestConfig {
        @Bean
        public WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }

        @Bean(name = "kakaoWebClient")
        public WebClient kakaoWebClient(WebClient.Builder webClientBuilder) {
            return webClientBuilder.build();
        }

        @Bean
        public KakaoProperties kakaoProperties() {
            return new KakaoProperties(
                "your_client_id",
                "your_redirect_uri",
                "https://kauth.kakao.com/oauth/token",
                "https://kapi.kakao.com/v2/user/me",
                "https://kapi.kakao.com/v2/api/talk/memo/default/send",
                "https://kauth.kakao.com"
            );
        }

        @Bean
        public KakaoService kakaoService(KakaoProperties kakaoProperties, @Qualifier("kakaoWebClient") WebClient webClient) {
            return new KakaoService(kakaoProperties, webClient);
        }
    }

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        KakaoProperties kakaoProperties = new KakaoProperties(
            "your_client_id",
            "your_redirect_uri",
            mockWebServer.url("/oauth/token").toString(),
            mockWebServer.url("/v2/user/me").toString(),
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            mockWebServer.url("/").toString()
        );

        kakaoService = new KakaoService(kakaoProperties, webClient.mutate().baseUrl(mockWebServer.url("/").toString()).build());
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetAccessToken_failure() {
        String authorizationCode = "invalid_code";

        // Mock the response from MockWebServer
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.BAD_REQUEST.value())
            .setBody("{\"error\":\"invalid_grant\"}")
            .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        KakaoServiceException exception = assertThrows(KakaoServiceException.class, () -> {
            kakaoService.getAccessToken(authorizationCode);
        });

        assertThat(exception.getCause()).isInstanceOf(WebClientResponseException.class);
        WebClientResponseException cause = (WebClientResponseException) exception.getCause();
        assertThat(cause.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetUserEmail_failure() {
        String accessToken = "invalid_token";

        // Mock the response from MockWebServer
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.UNAUTHORIZED.value())
            .setBody("{\"error\":\"unauthorized\"}")
            .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        KakaoServiceException exception = assertThrows(KakaoServiceException.class, () -> {
            kakaoService.getUserEmail(accessToken);
        });

        assertThat(exception.getCause()).isInstanceOf(WebClientResponseException.class);
        WebClientResponseException cause = (WebClientResponseException) exception.getCause();
        assertThat(cause.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}