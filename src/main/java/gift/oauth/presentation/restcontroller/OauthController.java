package gift.oauth.presentation.restcontroller;

import gift.oauth.business.dto.KakaoParam;
import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.service.OAuthService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {
    private final OAuthService oauthService;
    private final Environment env;

    public OauthController(OAuthService oauthService, Environment env) {
        this.oauthService = oauthService;
        this.env = env;
    }

    @GetMapping
    public void kakao(@RequestParam String code) {
        OAuthParam param = new KakaoParam(env, code);
        oauthService.getAccessToken(param);
    }

}
