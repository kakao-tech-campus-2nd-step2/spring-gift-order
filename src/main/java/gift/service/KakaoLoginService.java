package gift.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class KakaoLoginService {

    public static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";

    public LinkedMultiValueMap<String, String> createTokenRequest(String clientId, String redirectUri, String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }
}
