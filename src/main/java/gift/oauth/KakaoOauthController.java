package gift.oauth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class KakaoOauthController {

    private final OauthService oauthService;
    private final KakaoOAuthConfigProperties configProperties;

    public KakaoOauthController(OauthService oauthService,
        KakaoOAuthConfigProperties configProperties) {
        this.oauthService = oauthService;
        this.configProperties = configProperties;
    }

    @GetMapping()
    public String getKakaoOauthPage(Model model) {
        model.addAttribute("redirectUri", configProperties.getRedirectUrl());
        model.addAttribute("restApiKey", configProperties.getClientId());
        return "kakaoOauth.html";
    }

    @GetMapping("/code")
    public ResponseEntity<KakaoToken> kakoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(oauthService.getKakaoToken(code));
    }

}
