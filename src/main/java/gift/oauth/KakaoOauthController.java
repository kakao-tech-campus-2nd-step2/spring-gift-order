package gift.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class KakaoOauthController {
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    private final OauthService oauthService;

    public KakaoOauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }


    @GetMapping()
    public String getKakaoOauthPage(Model model){
        model.addAttribute("redirectUri", redirectUrl);
        model.addAttribute("restApiKey", clientId);
        return "kakaoOauth.html";
    }

    @GetMapping("/code")
    public ResponseEntity<KakaoToken> kakoLogin(@RequestParam("code") String code){
        return ResponseEntity.ok(oauthService.getKakaoToken(code));
    }

}
