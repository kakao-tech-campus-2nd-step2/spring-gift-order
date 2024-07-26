package gift.oauth.business.client;

import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();

    String getAccessToken(OAuthParam param);

    String getEmail(String accessToken, OAuthParam param);
}
