package gift.authentication.restapi;

import gift.authentication.restapi.dto.response.LoginResponse;
import gift.core.domain.authentication.OAuthService;
import gift.core.domain.authentication.OAuthType;
import gift.core.domain.authentication.Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoOAuthController {
    private final OAuthService oAuthService;

    public KakaoOAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/api/oauth/kakao")
    public LoginResponse login(@RequestParam("code") String code) {
        Token token = oAuthService.authenticate(OAuthType.KAKAO, code);

        return LoginResponse.of(token);
    }
}
