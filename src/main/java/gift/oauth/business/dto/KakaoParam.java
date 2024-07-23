package gift.oauth.business.dto;

import org.springframework.core.env.Environment;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoParam implements OAuthParam {
    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String code;

    public KakaoParam(Environment env, String code) {
        this.grantType = env.getProperty("spring.oauth.kakao.grant_type");
        this.clientId = env.getProperty("spring.oauth.kakao.client_id");
        this.redirectUri = env.getProperty("spring.oauth.kakao.redirect_uri");
        this.code = code;
    }

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> getTokenRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }
}
