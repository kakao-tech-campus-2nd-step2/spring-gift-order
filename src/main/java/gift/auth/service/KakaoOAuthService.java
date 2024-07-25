package gift.auth.service;

import gift.auth.persistence.OAuthAccessTokenRepository;
import gift.auth.persistence.OAuthRefreshTokenRepository;
import gift.auth.service.dto.KakaoTokenResponse;
import gift.auth.service.dto.KakaoUserInfoResponse;
import gift.config.KakaoOauthConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuthService {
    private final KakaoOauthConfig kakaoOauthConfig;
    private final OAuthAccessTokenRepository OAuthAccessTokenRepository;
    private final OAuthRefreshTokenRepository OAuthRefreshTokenRepository;

    public KakaoOAuthService(KakaoOauthConfig kakaoOauthConfig, OAuthAccessTokenRepository OAuthAccessTokenRepository,
                             OAuthRefreshTokenRepository OAuthRefreshTokenRepository) {
        this.kakaoOauthConfig = kakaoOauthConfig;
        this.OAuthAccessTokenRepository = OAuthAccessTokenRepository;
        this.OAuthRefreshTokenRepository = OAuthRefreshTokenRepository;
    }

    public String getKakaoLoginUrl() {
        return kakaoOauthConfig.createLoginUrl();
    }

    public void callBack(final String code) {
        var kakaoTokenResponse = getKakaoTokenResponse(code);
        var userInfo = getKakaoUserInfoResponse(kakaoTokenResponse.getAccessTokenWithTokenType());

        var accessToken = kakaoTokenResponse.toAccessTokenFrom(userInfo.id());
        var refreshToken = kakaoTokenResponse.toRefreshTokenFrom(userInfo.id());

        OAuthAccessTokenRepository.save(accessToken);
        OAuthRefreshTokenRepository.save(refreshToken);
    }

    private KakaoTokenResponse getKakaoTokenResponse(final String code) {
        var client = kakaoOauthConfig.createTokenClient();

        ResponseEntity<KakaoTokenResponse> response = client.post()
                .body(kakaoOauthConfig.createBody(code))
                .retrieve().toEntity(KakaoTokenResponse.class);

        return response.getBody();
    }

    private KakaoUserInfoResponse getKakaoUserInfoResponse(final String accessToken) {
        var userInfoClient = kakaoOauthConfig.createUserInfoClient();

        var userInfo = userInfoClient.get()
                .header("Authorization", accessToken)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class);

        return userInfo.getBody();
    }
}