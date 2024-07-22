package gift.auth.api;

import gift.auth.application.KakaoAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String kakaoCallback(@RequestParam("code") String code, Model model) throws Exception {
        String token = kakaoAuthService.getAccessToken(code);
        model.addAttribute("token", token);
        model.addAttribute("userInfo", kakaoAuthService.getUserInfo(token));
        return "redirect:/members/login";
    }

}
