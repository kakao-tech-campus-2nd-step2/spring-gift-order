package gift.oauth.business.dto;

import org.springframework.util.MultiValueMap;

public interface OAuthParam {

    OAuthProvider oAuthProvider();

    MultiValueMap<String, String> getTokenRequestBody();

    MultiValueMap<String, String> getEmailRequestBody();

    String secretKey();
}
