package gift;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.domain.model.dto.TokenResponseDto;
import gift.service.KakaoLoginService;
import gift.service.UserService;
import gift.controller.KakaoLoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class KakaoLoginControllerTest {

    @Mock
    private KakaoLoginService kakaoLoginService;

    @Mock
    private UserService userService;

    private KakaoLoginController kakaoLoginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kakaoLoginController = new KakaoLoginController(kakaoLoginService, userService);
    }

    @Test
    void testCallback() {
        // Given
        String code = "test_code";
        String accessToken = "test_access_token";
        TokenResponseDto expectedTokenResponse = new TokenResponseDto("jwt_token");

        when(kakaoLoginService.getAccessTokenFromKakao(code)).thenReturn(accessToken);
        when(userService.loginOrRegisterKakaoUser(accessToken)).thenReturn(expectedTokenResponse);

        // When
        ResponseEntity<TokenResponseDto> response = kakaoLoginController.callback(code);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedTokenResponse, response.getBody());

        verify(kakaoLoginService).getAccessTokenFromKakao(code);
        verify(userService).loginOrRegisterKakaoUser(accessToken);
    }
}