package gift.user.service;

import gift.user.client.KakaoLoginClient;
import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.repository.UserJpaRepository;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;

@Service
public class KakaoUserService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final KakaoLoginClient kakaoLoginClient;
    private final UserService userService;
    private final UserJpaRepository userRepository;

    public KakaoUserService(KakaoLoginClient kakaoLoginClient, UserService userService,
        UserJpaRepository userRepository) {
        this.kakaoLoginClient = kakaoLoginClient;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public UserResponse loginKakaoUser(String code) {
        var token = kakaoLoginClient.getKakaoTokenResponse(code).accessToken();
        var userInfo = kakaoLoginClient.getKakaoUserId(token);
        String email = userInfo.id() + "@kakao.com";
        String password = generateDummyPassword(userInfo.id());

        if (!userRepository.existsByEmail(email)) {
            userService.registerUser(new UserRegisterRequest(email, password));
        }

        return userService.loginUser(new UserLoginRequest(email, password));
    }

    private String generateDummyPassword(Long id) {
        RANDOM.setSeed(id);
        byte[] array = new byte[20];
        RANDOM.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
