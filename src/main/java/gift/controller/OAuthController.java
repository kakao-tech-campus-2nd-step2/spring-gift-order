package gift.controller;

import gift.dto.LoginResponse;
import gift.dto.OAuthLoginRequest;
import gift.service.OAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
    private final OAuthService oAuthService;
    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/")
    public LoginResponse oAuthLogin(@RequestParam("code") String code) {
        String accessToken = oAuthService.getAccessToken(code);
        String memberProfileId = oAuthService.getMemberProfileId(accessToken);
        OAuthLoginRequest request = new OAuthLoginRequest("kakao_" + memberProfileId);
        if (!oAuthService.isMemberAlreadyRegistered(request)) {
            oAuthService.register(request);
        }
        return oAuthService.login(request);
    }
}