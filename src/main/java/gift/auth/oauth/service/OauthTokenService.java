package gift.auth.oauth.service;

import gift.auth.AuthProvider;
import gift.auth.oauth.entity.OauthToken;
import gift.auth.oauth.repository.OauthTokenJpaRepository;
import gift.domain.user.entity.User;
import gift.exception.InvalidAuthException;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoToken;
import org.springframework.stereotype.Service;

@Service
public class OauthTokenService {

    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final KakaoApiProvider kakaoApiProvider;

    public OauthTokenService(OauthTokenJpaRepository oauthTokenJpaRepository,
        KakaoApiProvider kakaoApiProvider) {
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.kakaoApiProvider = kakaoApiProvider;
    }

    public void save(OauthToken oauthToken) {
        oauthTokenJpaRepository.save(oauthToken);
    }

    public OauthToken findByUserAndProvider(User user, AuthProvider provider) {
        return oauthTokenJpaRepository.findByUserAndProvider(user, provider)
            .orElseThrow(() -> new InvalidAuthException("error.invalid.token"));
    }

    public OauthToken renew(OauthToken oauthToken) {
        KakaoToken kakaoToken = kakaoApiProvider.renewToken(oauthToken);

        oauthToken.updateInfo(kakaoToken.accessToken(), kakaoToken.refreshToken());
        return oauthTokenJpaRepository.save(oauthToken);
    }
}
