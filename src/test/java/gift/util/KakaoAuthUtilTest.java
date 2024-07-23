package gift.util;

import gift.auth.util.KakaoAuthUtil;
import gift.auth.vo.KakaoProperties;
import gift.global.security.JwtFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KakaoAuthUtilTest {

    @InjectMocks
    private KakaoAuthUtil kakaoAuthUtil;

    @Mock
    private KakaoProperties kakaoProperties;

    @Test
    @DisplayName("카카오 인가 URL 반환 기능 테스트")
    void getKakaoAuthUrl() {
        String redirectUri = "test-uri";
        String clientId = "test-id";
        String url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&client_id="    + clientId;

        given(kakaoProperties.redirectUri())
                .willReturn(redirectUri);
        given(kakaoProperties.clientId())
                .willReturn(clientId);

        String kakaoAuthUrl = kakaoAuthUtil.getKakaoAuthUrl();

        assertThat(kakaoAuthUrl).isEqualTo(url);
        verify(kakaoProperties).redirectUri();
        verify(kakaoProperties).clientId();
    }

    @Test
    @DisplayName("POST 요청 메시지 반환 기능 테스트")
    void getRequestWithPost() {
        String grantType = "test-code";
        String redirectUri = "test-uri";
        String clientId = "test-id";
        String code = "test-auth";
        String url = "test@email.com";

        given(kakaoProperties.grantType())
                .willReturn(grantType);
        given(kakaoProperties.redirectUri())
                .willReturn(redirectUri);
        given(kakaoProperties.clientId())
                .willReturn(clientId);

        RequestEntity<LinkedMultiValueMap<String, String>> request = kakaoAuthUtil.getRequestWithPost(url, code);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getHeaders()
                .get(HttpHeaders.CONTENT_TYPE)
                .getFirst()).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        assertThat(request.getBody()
                .values())
                .contains(
                        List.of(grantType),
                        List.of(redirectUri),
                        List.of(clientId),
                        List.of(code)
                );
    }

    @Test
    @DisplayName("GET 요청 메시지 반환 기능 테스트")
    void getRequestWithGet() {
        String url = "test@email.com";
        String token = "test-token";
        String authHeader = JwtFilter.BEAR_PREFIX + token;

        RequestEntity<Void> request = kakaoAuthUtil.getRequestWithGet(url, token);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .getFirst()).isEqualTo(authHeader);
    }

}