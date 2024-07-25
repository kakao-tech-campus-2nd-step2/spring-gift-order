package gift.oauth.presentation.restcontroller;

import gift.oauth.business.dto.KakaoParam;
import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.service.OAuthService;
import gift.oauth.presentation.config.KakaoConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {
    private final OAuthService oauthService;
    private final KakaoConfig kakaoConfig;

    public OauthController(OAuthService oauthService, KakaoConfig kakaoConfig) {
        this.oauthService = oauthService;
        this.kakaoConfig = kakaoConfig;
    }

    @GetMapping
    public void kakao(@RequestParam String code) {
        OAuthParam param = new KakaoParam(kakaoConfig, code);
        oauthService.getAccessToken(param);
    }

}
