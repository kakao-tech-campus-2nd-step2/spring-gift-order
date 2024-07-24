package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.AccessAndRefreshToken;
import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.MemberDto;
import gift.product.exception.LoginFailedException;
import gift.product.model.Member;
import gift.product.property.KakaoProperties;
import gift.product.repository.AuthRepository;
import gift.product.service.AuthService;
import java.io.IOException;
import java.util.Properties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    final String EMAIL = "test@test.com";
    final String PASSWORD = "test";

    MockWebServer mockWebServer;

    ObjectMapper objectMapper;

    @Mock
    AuthRepository authRepository;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void ObjectMapper_셋팅() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void 카카오_프로퍼티_셋팅() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application-test.properties"));

        String grantType = properties.getProperty("kakao.grant-type");
        String clientId = properties.getProperty("kakao.client-id");
        String redirectUrl = properties.getProperty("kakao.redirect-url");

        ReflectionTestUtils.setField(authService, "kakaoProperties", new KakaoProperties(grantType, clientId, redirectUrl));
    }

    @BeforeEach
    void 시크릿_키_셋팅() {
        ReflectionTestUtils.setField(authService, "SECRET_KEY",
            "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=");
    }

    @BeforeEach
    void 가짜_API_서버_구동() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void 가짜_API_서버_종료() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void 회원가입() {
        //given
        MemberDto memberDto = new MemberDto(EMAIL, PASSWORD);
        given(authRepository.existsByEmail(EMAIL)).willReturn(false);

        //when
        authService.register(memberDto);

        //then
        then(authRepository).should().save(any());
    }

    @Test
    void 로그인() {
        //given
        MemberDto memberDto = new MemberDto(EMAIL, PASSWORD);
        given(authRepository.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL, PASSWORD));
        given(authRepository.existsByEmail(EMAIL)).willReturn(true);

        //when
        JwtResponse jwtResponse = authService.login(memberDto);

        //then
        assertThat(jwtResponse.token()).isNotEmpty();
    }

    @Test
    void 회원가입_중복() {
        //given
        MemberDto memberDto = new MemberDto(EMAIL, PASSWORD);
        given(authRepository.existsByEmail(EMAIL)).willReturn(false);
        authService.register(memberDto);
        given(authRepository.existsByEmail(EMAIL)).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.register(memberDto)).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    void 토큰_발급() throws JsonProcessingException {
        //given
        AccessAndRefreshToken responseBody = new AccessAndRefreshToken("test_access_token",
            "test_refresh_token");
        mockWebServer.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(responseBody)));

        //when
        String mockUrl = mockWebServer.url("/oauth/token").toString();
        AccessAndRefreshToken response = authService.getAccessAndRefreshToken("test_authorization_code", mockUrl);

        //then
        assertSoftly(softly -> {
            assertThat(response.accessToken()).isEqualTo("test_access_token");
            assertThat(response.refreshToken()).isEqualTo("test_refresh_token");
        });
    }

    @Test
    void 카카오_유저_회원가입_처리() {
        //given
        String testEmail = "test_email";
        given(authRepository.existsByEmail(testEmail)).willReturn(false);

        String responseBody = "{\"kakao_account\":{\"email\":\"" + testEmail + "\"}}";
        mockWebServer.enqueue(new MockResponse().setBody(responseBody));

        //when
        String mockUrl = mockWebServer.url("/v2/user/me").toString();
        authService.registerKakaoMember("test_access_token", mockUrl);

        //then
        then(authRepository).should().save(any());
    }

    @Test
    void 카카오_유저_연결_끊기() {
        //given
        long testId = 1L;
        String responseBody = "{\"id\":\"" + testId + "\"}";
        mockWebServer.enqueue(new MockResponse().setBody(responseBody));

        //when
        String mockUrl = mockWebServer.url("/v1/user/unlink").toString();
        Long id = authService.unlinkKakaoAccount("test_access_token", mockUrl);

        //then
        assertThat(id).isEqualTo(testId);
    }

    @Test
    void 존재하지_않는_회원_로그인() {
        //given
        MemberDto memberDto = new MemberDto(EMAIL, PASSWORD);
        given(authRepository.existsByEmail(EMAIL)).willReturn(false);

        //when, then
        assertThatThrownBy(() -> authService.login(memberDto)).isInstanceOf(
            LoginFailedException.class);
    }

    @Test
    void 카카오_토큰_발급_API_에러() {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        String mockUrl = mockWebServer.url("/oauth/token").toString();

        //when, then
        assertThatThrownBy(() -> authService.getAccessAndRefreshToken("test_authorization_code", mockUrl)).isInstanceOf(
            LoginFailedException.class);
    }

    @Test
    void 카카오_사용자_정보_조회_API_에러() {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        String mockUrl = mockWebServer.url("/v2/user/me").toString();

        //when, then
        assertThatThrownBy(() -> authService.registerKakaoMember("test_access_token", mockUrl)).isInstanceOf(
            LoginFailedException.class);
    }

    @Test
    void 카카오_사용자_연결_끊기_API_에러() {
        //given
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        String mockUrl = mockWebServer.url("/v1/user/unlink").toString();

        //when, then
        assertThatThrownBy(() -> authService.unlinkKakaoAccount("test_access_token", mockUrl)).isInstanceOf(
            LoginFailedException.class);
    }
}
