package gift.controller.auth;

import gift.service.KakaoOauthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final KakaoOauthService kakaoOauthService;

    public AuthController(KakaoOauthService kakaoOauthService) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @GetMapping("login/oauth/kakao")
    public String getKakaoAuthorizationCode(){
        String param = kakaoOauthService.makeKakaOauthParameter().toString();
        return "redirect:https://kauth.kakao.com/oauth/authorize?" + param;
    }
}
