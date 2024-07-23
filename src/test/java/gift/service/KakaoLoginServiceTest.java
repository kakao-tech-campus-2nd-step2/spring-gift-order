package gift.service;

import gift.service.kakao.KakaoLoginService;
import gift.service.kakao.KakaoProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private KakaoProperties properties;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    @DisplayName("Redirect URL 을 header location 에 넣고 헤더를 반환한다.")
    @Test
    void getRedirectHeaders() throws Exception {
        //given
        String redirectUrl = "https://test.com";
        String clientId = "test-client-id";

        given(properties.redirectUrl()).willReturn(redirectUrl);
        given(properties.clientId()).willReturn(clientId);

        //when
        HttpHeaders headers = kakaoLoginService.getRedirectHeaders();

        //then
        String expectedUrl = "https://kauth.kakao.com/oauth/authorize?&response_type=code"
                + "&redirect_uri=" + redirectUrl
                + "&client_id=" + clientId;

        assertThat(headers.getLocation().toString()).isEqualTo(expectedUrl);
        then(properties).should().redirectUrl();
        then(properties).should().clientId();
    }

}
