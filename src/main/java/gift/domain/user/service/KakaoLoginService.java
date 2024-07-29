package gift.domain.user.service;

import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.user.repository.OauthTokenJpaRepository;
import gift.domain.user.repository.UserJpaRepository;
import gift.exception.InvalidUserInfoException;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoLoginService implements OauthLoginService {

    private final OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider;
    private final UserJpaRepository userJpaRepository;
    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(
        OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider,
        UserJpaRepository userJpaRepository,
        OauthTokenJpaRepository oauthTokenJpaRepository,
        JwtProvider jwtProvider
    ) {
        this.oauthApiProvider = oauthApiProvider;
        this.userJpaRepository = userJpaRepository;
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.jwtProvider = jwtProvider;
    }

    public String getAuthCodeUrl() {
        return oauthApiProvider.getAuthCodeUrl();
    }

    @Transactional
    public JwtToken login(String code) {
        KakaoToken kakaoToken = oauthApiProvider.getToken(code);
        String accessToken = kakaoToken.accessToken();
        String refreshToken = kakaoToken.refreshToken();

        oauthApiProvider.validateAccessToken(accessToken);
        KakaoUserInfo userInfo = oauthApiProvider.getUserInfo(accessToken);

        String email = userInfo.kakaoAccount().email();
        String name = userInfo.kakaoAccount().profile().nickname();

        return userJpaRepository.findByEmail(email)
            .map(user -> {
                if (user.getAuthProvider() != AuthProvider.KAKAO) {
                    throw new InvalidUserInfoException("error.invalid.userinfo.provider");
                }
                oauthTokenJpaRepository.save(new OauthToken(null, user, AuthProvider.KAKAO, accessToken, refreshToken));
                return jwtProvider.generateToken(user);
            })
            .orElseGet(() -> signUp(name, email, accessToken, refreshToken));
    }

    private JwtToken signUp(String name, String email, String accessToken, String refreshToken) {
        User user = new User(null, name, email, "kakao", Role.USER, AuthProvider.KAKAO);
        User savedUser = userJpaRepository.save(user);

        oauthTokenJpaRepository.save(new OauthToken(null, savedUser, AuthProvider.KAKAO, accessToken, refreshToken));

        return jwtProvider.generateToken(savedUser);
    }
}
