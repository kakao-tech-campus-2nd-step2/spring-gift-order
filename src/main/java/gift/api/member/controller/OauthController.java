package gift.api.member.controller;

import gift.api.member.KakaoProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/oauth")
public class OauthController {

    private final KakaoProperties kakaoProperties;

    public OauthController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public String requestLogin() {
        return "oauth";
    }

    @GetMapping("/kakao")
    public RedirectView requestAuthorizationCode() {
        return new RedirectView(
            "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080/members/oauth/kakao&client_id="
                + kakaoProperties.clientId());
    }
}
