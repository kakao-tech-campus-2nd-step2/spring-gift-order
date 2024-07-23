package gift.auth.api;

import gift.auth.application.KakaoAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping
    public String kakaoLogin() {
        return "redirect:" + kakaoAuthService.getKakaoAuthUrl();
    }

    @GetMapping("/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam("code") String code) throws Exception {
        String token = kakaoAuthService.getAccessToken(code);
        return kakaoAuthService.getUserInfo(token);
    }

}
