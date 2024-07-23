package gift.application;

import gift.auth.application.KakaoClient;
import gift.auth.vo.KakaoProperties;
import gift.global.config.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoClient.class)
class KakaoClientTest {

    @Autowired
    private KakaoClient kakaoClient;

    @MockBean
    private KakaoProperties kakaoProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("액세스 토큰 발급 확인 테스트")
    void getAccessToken() {
        String code = "test-code";
        String responseBody = "{ \"access_token\": \"test-token\"}";
        String responseToken = "test-token";
        String url = "https://kauth.kakao.com/oauth/token";

        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        String token = kakaoClient.getAccessToken(code);

        assertThat(token).isEqualTo(responseToken);
        server.verify();
    }

    @Test
    @DisplayName("카카오 사용자 ID 확인 테스트")
    void getUserId() {
        String token = "test-token";
        String responseBody = "{ \"id\": \"123\"}";
        String url = "https://kapi.kakao.com/v2/user/me";
        String authPrefix = "Bearer ";
        String authHeader = authPrefix + token;
        Long userInfoId = 123L;
        given(kakaoProperties.authorizationPrefix())
                .willReturn(authPrefix);
        server.expect(requestTo(url))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        Long userInfoResponse = kakaoClient.getUserId(token);

        assertThat(userInfoResponse).isEqualTo(userInfoId);
        server.verify();
    }

}