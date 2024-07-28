package gift.domain.user.service;

import gift.auth.jwt.JwtToken;

public interface OauthLoginService {

    String getAuthCodeUrl();
    JwtToken login(String code);
}
