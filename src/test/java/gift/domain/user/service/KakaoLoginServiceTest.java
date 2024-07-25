package gift.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.auth.dto.Token;
import gift.domain.user.dto.UserDto;
import gift.domain.user.dto.UserLoginDto;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import gift.external.api.kakao.dto.KakaoUserInfo.KakaoAccount;
import gift.external.api.kakao.dto.KakaoUserInfo.Profile;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class KakaoLoginServiceTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private KakaoApiProvider kakaoApiProvider;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("카카오 로그인 서비스 테스트")
    void login() {
        // given
        Token expected = new Token("testToken");
        UserDto userDto = new UserDto(null, "testUser", "test@test.com", "test123", null);
        KakaoToken kakaoToken = new KakaoToken(null, "testAccessToken", null, null, null);
        KakaoUserInfo testUserInfo =
            new KakaoUserInfo(
                102345L, new KakaoAccount(
                new Profile("testUser"),
                true,
                "test@test.com"));

        given(kakaoApiProvider.getToken(eq("testCode"))).willReturn(kakaoToken);
        doNothing().when(kakaoApiProvider).validateAccessToken(eq("testAccessToken"));
        given(kakaoApiProvider.getUserInfo(eq("testAccessToken"))).willReturn(testUserInfo);

        given(userService.findByEmail(eq("test@test.com"))).willReturn(Optional.of(userDto));
        given(userService.login(any(UserLoginDto.class))).willReturn(expected);

        // when
        Token actual = kakaoLoginService.login("testCode");

        // then
        assertThat(actual).isEqualTo(expected);
    }
}