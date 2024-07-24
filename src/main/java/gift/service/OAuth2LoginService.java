package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.response.oAuth2TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;

public interface OAuth2LoginService {

    void checkRedirectUriParams(HttpServletRequest request);

    oAuth2TokenResponse getToken(String code);

    LinkedMultiValueMap<String, String> createTokenRequest(String clientId,
        String redirectUri, String code);
}
