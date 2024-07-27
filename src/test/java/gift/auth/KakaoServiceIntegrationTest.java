package gift.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class KakaoServiceIntegrationTest {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private KakaoOauthProperty kakaoOauthProperty;

    @Test
    void getKakaoRedirectUrl_통합테스트() {
        // When
        String redirectUrl = kakaoService.getKakaoRedirectUrl();

        System.out.println("redirectUrl = " + redirectUrl);

        // Then
        assertThat(redirectUrl).isEqualTo("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
                kakaoOauthProperty.clientId() + "&redirect_uri=" + kakaoOauthProperty.redirectUri() + "&scope=" + String.join(",", kakaoOauthProperty.scope()).replace("\"", ""));
    }

    @Test
    void fetchToken_통합테스트() {
        // Given
        String code = "K8YXzE7YkqVV8KTyg--HtiZEDtzj79rke-LJDS13pWWm1HCGROxcXAAAAAQKKiVRAAABkOUWkwykJA3lYdtGWQ";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("redirect_uri", kakaoOauthProperty.redirectUri());
        formData.add("code", code);

        // When
        KakaoToken token = kakaoService.fetchToken(code);
        System.out.println("token = " + token);
        /**
         * token =
             * KakaoToken
                 * [
                     * tokenType=bearer,
                     * accessToken=_EWAeY_38JLE5pHYpMzy29ylbFfQrenIAAAAAQo9cxcAAAGQ5Rb4nxamEcnPBcmr,
                     * idToken=null,
                     * expiresIn=21599,
                     * refreshToken=1FSGDX9YLSIdja4gfN1C5JQh4uaqc8X3AAAAAgo9cxcAAAGQ5Rb4nBamEcnPBcmr,
                     * refreshTokenExpiresIn=5183999, scope=account_email profile_nickname
                 * ]
         */

        // Then
        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    void refreshToken_통합테스트() {
        // Given
        String refreshToken = "1FSGDX9YLSIdja4gfN1C5JQh4uaqc8X3AAAAAgo9cxcAAAGQ5Rb4nBamEcnPBcmr";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("refresh_token", refreshToken);

        // When
        KakaoToken token = kakaoService.refreshToken(refreshToken);
        System.out.println("token = " + token);

        // Then
        assertThat(token).isNotNull();
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    void fetchMemberInfo_통합테스트() {
        // Given
        String accessToken = "_EWAeY_38JLE5pHYpMzy29ylbFfQrenIAAAAAQo9cxcAAAGQ5Rb4nxamEcnPBcmr";

        // When
        KakaoResponse response = kakaoService.fetchMemberInfo(accessToken);
        System.out.println("response = " + response);

        /**
         * KakaoResponse
             * [
                 * id=3636131132,
                 * kakaoAccount=
                     * KakaoAccount
                        * [
                         * profile=KakaoProfile[nickname=김유겸],
                         * email=rladbrua0207@gmail.com
                        *]
             * ]
         */

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
    }

    @Test
    void unlink_및_unlink_후_연결_통합테스트() {
        // Given
        Long userId = 3636131132L;
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", String.valueOf(userId));

        // When
        kakaoService.unlink(userId);

        // Then
        assertThat(userId).isNotNull();

        // unlink 후 연결 테스트
        String accessToken = "_EWAeY_38JLE5pHYpMzy29ylbFfQrenIAAAAAQo9cxcAAAGQ5Rb4nxamEcnPBcmr";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.fetchMemberInfo(accessToken);
        });

        assertThat(exception.getMessage()).isEqualTo("401 Unauthorized: \"{\"msg\":\"this access token does not exist\",\"code\":-401}\"");
    }
}
