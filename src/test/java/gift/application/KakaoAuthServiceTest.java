package gift.application;

import gift.auth.application.KakaoAuthService;
import gift.auth.vo.KakaoProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class KakaoAuthServiceTest {

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @Mock
    private KakaoProperties kakaoProperties;

    @MockBean
    private RestClient restClient;

    @Test
    @DisplayName("카카오 인가 URL 확인 테스트")
    void getKakaoAuthUrl() {
        String redirectUri = "test_uri@email.com";
        String clientId = "test_client";
        given(kakaoProperties.getRedirectUri())
                .willReturn(redirectUri);
        given(kakaoProperties.getClientId())
                .willReturn(clientId);
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&client_id="    + clientId;

        String kakaoAuthUrl = kakaoAuthService.getKakaoAuthUrl();

        assertThat(kakaoAuthUrl).isEqualTo(url);
    }

}