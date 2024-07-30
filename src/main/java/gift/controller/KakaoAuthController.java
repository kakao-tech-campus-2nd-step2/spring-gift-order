package gift.controller;

import gift.dto.KakaoTokenResponse;
import gift.service.KakaoAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin() {
        return "redirect:" + kakaoAuthService.getAuthorizationUri();
    }

    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(@RequestParam String code, Model model) {
        try {
            KakaoTokenResponse tokenResponse = kakaoAuthService.getAccessToken(code);
            String accessToken = tokenResponse.getAccessToken();
            String jwtToken = kakaoAuthService.handleKakaoLogin(accessToken);

            model.addAttribute("accessToken", accessToken);
            model.addAttribute("jwtToken", jwtToken);

            return "loginSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
