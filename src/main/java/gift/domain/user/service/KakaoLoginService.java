package gift.domain.user.service;

import gift.auth.AuthProvider;
import gift.auth.dto.Token;
import gift.auth.jwt.JwtProvider;
import gift.auth.oauth.entity.OauthToken;
import gift.auth.oauth.service.OauthTokenService;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.user.repository.UserJpaRepository;
import gift.exception.InvalidUserInfoException;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Service;

@Service
public class KakaoLoginService {

    private final KakaoApiProvider kakaoApiProvider;
    private final UserJpaRepository userJpaRepository;
    private final OauthTokenService oauthTokenService;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(
        KakaoApiProvider kakaoApiProvider,
        UserJpaRepository userJpaRepository,
        OauthTokenService oauthTokenService,
        JwtProvider jwtProvider)
    {
        this.kakaoApiProvider = kakaoApiProvider;
        this.userJpaRepository = userJpaRepository;
        this.oauthTokenService = oauthTokenService;
        this.jwtProvider = jwtProvider;
    }

    public String getAuthCodeUrl() {
        return kakaoApiProvider.getAuthCodeUrl();
    }

    public Token login(String code) {
        KakaoToken kakaoToken = kakaoApiProvider.getToken(code);
        String accessToken = kakaoToken.accessToken();
        String refreshToken = kakaoToken.refreshToken();

        kakaoApiProvider.validateAccessToken(accessToken);
        KakaoUserInfo userInfo = kakaoApiProvider.getUserInfo(accessToken);

        String email = userInfo.kakaoAccount().email();
        String name = userInfo.kakaoAccount().profile().nickname();

        return userJpaRepository.findByEmail(email)
            .map(user -> {
                if (user.getAuthProvider() != AuthProvider.KAKAO) {
                    throw new InvalidUserInfoException("error.invalid.userinfo.provider");
                }
                oauthTokenService.save(new OauthToken(null, user, AuthProvider.KAKAO, accessToken, refreshToken));
                return jwtProvider.generateToken(user);
            })
            .orElseGet(() -> signUp(name, email, accessToken, refreshToken));
    }

    private Token signUp(String name, String email, String accessToken, String refreshToken) {
        User user = new User(null, name, email, "kakao", Role.USER, AuthProvider.KAKAO);
        User savedUser = userJpaRepository.save(user);

        oauthTokenService.save(new OauthToken(null, savedUser, AuthProvider.KAKAO, accessToken, refreshToken));

        return jwtProvider.generateToken(savedUser);
    }
}
