package gift.permission.user.service;

import gift.global.dto.TokenDto;
import gift.permission.user.component.AdminIdProperties;
import gift.permission.user.entity.User;
import gift.permission.user.repository.UserRepository;
import gift.token.component.TokenComponent;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// UserController로부터 입력을 받아서 엔터티를 사용해서 비즈니스 로직 수행
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenComponent tokenComponent;
    private final AdminIdProperties adminIdProperties;

    @Autowired
    public UserService(UserRepository userRepository,
        TokenComponent tokenComponent, AdminIdProperties adminIdProperties) {
        this.userRepository = userRepository;
        this.tokenComponent = tokenComponent;
        this.adminIdProperties = adminIdProperties;
    }

    // 회원가입 비즈니스 로직 처리.
    @Transactional
    public TokenDto register(String userId, String platform, String refreshToken,
        int refreshTokenExpiry) {
        var isAdmin = userId.equals(adminIdProperties.getAdminId(platform));
        userRepository.save(new User(userId, isAdmin, refreshToken, refreshTokenExpiry));

        var accessToken = tokenComponent.getToken(userId, isAdmin);
        return new TokenDto(accessToken, refreshToken);
    }

    // 로그인 비즈니스 로직 처리
    @Transactional
    public TokenDto login(long planeUserId, String platform, String refreshToken,
        int refreshTokenExpiry) {
        // 정말로 고유한 키를 생성
        var userId = makeUserId(planeUserId, platform);

        // 키를 통해서 User 정보 가져옴.
        var user = userRepository.findById(userId);

        // 회원가입 되지 않았다면
        if (user.isEmpty()) {
            return register(userId, platform, refreshToken, refreshTokenExpiry);
        }

        // 가입된 유저였다면 로그인 (refreshToken과 만료시간을 update하고 token dto 반환)
        var actualUser = user.get();
        actualUser.login(refreshToken, refreshTokenExpiry);

        // 토큰 반환하기
        var accessToken = tokenComponent.getToken(actualUser.getUserId(), actualUser.getIsAdmin());
        return new TokenDto(accessToken, refreshToken);
    }

    // refresh token으로 액세스 토큰 재발급 받는 메서드.
    @Transactional
    public String getAccessToken(String refreshToken) {
        var actualUser = userRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토큰입니다."));

        // 토큰이 만료됐는지 검증
        verifyRefreshTokenExpiry(actualUser);

        // 멀쩡하다면 액세스 토큰 재발급
        return tokenComponent.getToken(actualUser.getUserId(), actualUser.getIsAdmin());
    }

    // 플랫폼 별로 고유한 id를 제공하지만, 플랫폼이 다르다면 고유하지 않을 수도 있으므로 다음과 같이 새로운 id를 생성.
    private String makeUserId(long planeUserId, String platform) {
        return platform + planeUserId;
    }

    // 리프레시 토큰마저 만료됐다면 재로그인 요청
    private void verifyRefreshTokenExpiry(User user) {
        var expiry = user.getRefreshTokenExpiry();
        if (expiry.before(new Date())) {
            throw new IllegalArgumentException("다시 로그인 하세요.");
        }
    }
}