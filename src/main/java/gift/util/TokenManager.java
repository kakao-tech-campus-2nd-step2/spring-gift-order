package gift.util;

import gift.dto.OAuth.AuthTokenInfoResponse;
import gift.dto.OAuth.AuthTokenResponse;
import gift.model.token.KakaoToken;
import gift.repository.token.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final AuthUtil authUtil;

    private final KakaoTokenRepository kakaoTokenRepository;

    public TokenManager(AuthUtil authUtil, KakaoTokenRepository kakaoTokenRepository) {
        this.authUtil = authUtil;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public KakaoToken checkExpiredToken(KakaoToken kakaoToken){
        AuthTokenInfoResponse tokenInfo = authUtil.getTokenInfo(kakaoToken.getAccessToken());
        if(tokenInfo.expiresIn() == 0){
            AuthTokenResponse tokenResponse = authUtil.refreshAccessToken(kakaoToken.getRefreshToken());
            kakaoToken.updateAccessToken(tokenResponse.accessToken());
            kakaoTokenRepository.save(kakaoToken);
            return kakaoToken;
        }
        return kakaoToken;
    }

}
