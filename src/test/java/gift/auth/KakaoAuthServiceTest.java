package gift.auth;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoUserResponse;
import gift.entity.KakaoUser;
import gift.entity.User;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class KakaoAuthServiceTest {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @MockBean
    private KakaoApiClient kakaoApiClient;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KakaoUserRepository kakaoUserRepository;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private KakaoProperties kakaoProperties;

    private KakaoUserResponse kakaoUserResponse;
    private User user;
    private KakaoUser kakaoUser;

    @BeforeEach
    public void setup() {
        kakaoUserResponse = new KakaoUserResponse(12345L, new KakaoUserResponse.Properties("testNickname"));

        user = new User(1L, "test@example.com", "password");

        kakaoUser = new KakaoUser(kakaoUserResponse.getId(), kakaoUserResponse.getProperties().getNickname(), user);
        user.setKakaoUser(kakaoUser);
    }

    @Test
    public void 카카오_로그인_URL_생성_성공() {
        String loginUrl = kakaoAuthService.getLoginUrl();
        assertNotNull(loginUrl);
        assertTrue(loginUrl.contains("client_id=" + kakaoProperties.getClientId()));
    }

    @Test
    public void 카카오_콜백_처리_성공() {
        Mockito.when(kakaoApiClient.getAccessToken(anyString())).thenReturn("mockAccessToken");
        Mockito.when(kakaoApiClient.getUserInfo(anyString())).thenReturn(kakaoUserResponse);

        Mockito.when(kakaoUserRepository.findByKakaoId(anyLong())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(kakaoUserRepository.save(Mockito.any(KakaoUser.class))).thenReturn(kakaoUser);
        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockJwtToken");

        Map<String, String> tokens = kakaoAuthService.handleCallback("mockAuthorizationCode");

        assertNotNull(tokens);
        assertEquals("mockAccessToken", tokens.get("kakaoAccessToken"));
        assertEquals("mockJwtToken", tokens.get("jwtToken"));
    }

    @Test
    public void 토큰_생성_성공() {
        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        String token = kakaoAuthService.generateToken(user);

        assertEquals("mockToken", token);
    }
}
