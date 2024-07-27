package gift.util;

import gift.dto.OAuth.AuthTokenInfoResponse;
import gift.dto.OAuth.AuthTokenResponse;
import gift.model.token.OAuthToken;
import gift.repository.token.OAuthTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final AuthUtil authUtil;

    private final OAuthTokenRepository OAuthTokenRepository;

    public TokenManager(AuthUtil authUtil, OAuthTokenRepository OAuthTokenRepository) {
        this.authUtil = authUtil;
        this.OAuthTokenRepository = OAuthTokenRepository;
    }

    public OAuthToken checkExpiredToken(OAuthToken OAuthToken){
        AuthTokenInfoResponse tokenInfo = authUtil.getTokenInfo(OAuthToken.getAccessToken());
        if(tokenInfo.expiresIn() == 0){
            AuthTokenResponse tokenResponse = authUtil.refreshAccessToken(OAuthToken.getRefreshToken());
            OAuthToken.updateAccessToken(tokenResponse.accessToken());
            OAuthTokenRepository.save(OAuthToken);
            return OAuthToken;
        }
        return OAuthToken;
    }

}
