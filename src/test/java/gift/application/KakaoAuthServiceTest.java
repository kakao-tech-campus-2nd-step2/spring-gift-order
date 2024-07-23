package gift.application;

import gift.auth.application.KakaoAuthService;
import gift.auth.util.KakaoAuthUtil;
import gift.global.config.RestTemplateConfig;
import gift.global.security.JwtFilter;
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
import org.springframework.http.RequestEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoAuthService.class)
class KakaoAuthServiceTest {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @MockBean
    private KakaoAuthUtil kakaoAuthUtil;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("카카오 인가 URL 확인 테스트")
    void getKakaoAuthUrl() {
        String redirectUri = "test_uri@email.com";
        String clientId = "test_client";
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&client_id="    + clientId;
        given(kakaoAuthUtil.getKakaoAuthUrl())
                .willReturn(url);

        String kakaoAuthUrl = kakaoAuthService.getKakaoAuthUrl();

        assertThat(kakaoAuthUrl).isEqualTo(url);
        verify(kakaoAuthUtil).getKakaoAuthUrl();
    }

    @Test
    @DisplayName("액세스 토큰 발급 확인 테스트")
    void getAccessToken() throws Exception {
        String code = "test-code";
        String responseBody = "{ \"access_token\": \"test-token\"}";
        String responseToken = "test-token";
        String url = "https://kauth.kakao.com/oauth/token";

        RequestEntity<LinkedMultiValueMap<String, String>> requestBody = RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(new LinkedMultiValueMap<String, String>());
        given(kakaoAuthUtil.getRequestWithPost(anyString(), anyString()))
                .willReturn(requestBody);
        given(kakaoAuthUtil.getValueOfJsonByKey(anyString(), anyString()))
                .willReturn(responseToken);

        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED + ";charset=UTF-8"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        String token = kakaoAuthService.getAccessToken(code);

        assertThat(token).isEqualTo(responseToken);
        verify(kakaoAuthUtil).getRequestWithPost(url, code);
        verify(kakaoAuthUtil).getValueOfJsonByKey(responseBody, "access_token");
        server.verify();
    }

    @Test
    @DisplayName("사용자 정보 확인 테스트")
    void getUserInfo() throws Exception {
        String token = "test-token";
        String responseBody = "{ \"id\": \"123\"}";
        String url = "https://kapi.kakao.com/v2/user/me";
        String authHeader = JwtFilter.BEAR_PREFIX + token;
        String userInfoId = "123";

        RequestEntity<Void> requestBody = RequestEntity.get(url)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .build();

        given(kakaoAuthUtil.getRequestWithGet(anyString(), anyString()))
                .willReturn(requestBody);
        given(kakaoAuthUtil.getValueOfJsonByKey(anyString(), anyString()))
                .willReturn(userInfoId);

        server.expect(requestTo(url))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        String userInfoResponse = kakaoAuthService.getUserInfo(token);

        assertThat(userInfoResponse).isEqualTo(userInfoId);
        verify(kakaoAuthUtil).getRequestWithGet(url, token);
        verify(kakaoAuthUtil).getValueOfJsonByKey(responseBody, "id");
        server.verify();
    }

}