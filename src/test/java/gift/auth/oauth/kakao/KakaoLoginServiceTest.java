package gift.auth.oauth.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import gift.auth.dto.Token;
import gift.client.KakaoApiClient;
import gift.client.KakaoAuthClient;
import gift.config.RestClientConfig;
import gift.domain.user.dto.UserLoginDto;
import gift.domain.user.service.UserService;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@RestClientTest(value = { KakaoLoginService.class, KakaoProperties.class, RestClientConfig.class })
class KakaoLoginServiceTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Autowired
    private KakaoAuthClient kakaoAuthClient;

    @Autowired
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private KakaoProperties kakaoProperties;

    @MockBean
    private UserService userService;

    private static final String ACCESS_TOKEN = "accessToken";


    @Test
    @DisplayName("카카오 로그인 서비스 테스트")
    void login() {
        // given
        mockServer = MockRestServiceServer.bindTo(RestClient.builder()).build();

        // getAccessToken - 오류........
        mockServer.expect(requestTo(
            URI.create(kakaoProperties.authBaseUrl() + "/oauth/token")))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess("{\"access_token\": \"" + ACCESS_TOKEN + "\"}",
                MediaType.APPLICATION_JSON));

        // validateAccessToken
        mockServer.expect(requestTo(URI.create(kakaoProperties.apiBaseUrl() + "/v1/user/access_token_info")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());

        // getUserInfo
        mockServer.expect(requestTo(URI.create(kakaoProperties.apiBaseUrl() + "/v2/user/me")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"kakao_account\": {\"profile\": \"nickname\": \"testUser\"}, \"email\": \"testUser@test.com\"}",
                MediaType.APPLICATION_JSON));


        given(userService.findByEmail(anyString())).willReturn(Optional.empty());
        given(userService.login(any(UserLoginDto.class))).willReturn(new Token("testToken"));

        // when
        Token token = kakaoLoginService.login("testCode");

        // then
        assertThat(token.token()).isEqualTo("testToken");
    }
}