package gift.service;

import gift.response.oauth2.oAuth2TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;

public interface OAuth2LoginService {

    void checkRedirectUriParams(HttpServletRequest request);

    oAuth2TokenResponse getToken(String code);

    String getMemberInfo(String accessToken);

    LinkedMultiValueMap<String, String> createTokenRequest(String clientId,
        String redirectUri, String code);
}
