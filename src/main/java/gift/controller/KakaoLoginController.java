package gift.controller;

import gift.service.KakaoService;
import gift.util.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    @Autowired
    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }
    @GetMapping("/kakao/login")
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.redirectUrl());
        return "kakaoLogin";
    }

    @GetMapping("/kakao/oauth2/callback")
    public String callbackKakao(@RequestParam String code, Model model) {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        model.addAttribute("accessToken", accessToken);
        return "kakaoLoginSuccess";
    }

    @GetMapping("/kakao/loginSuccess")
    public String loginSuccess() {
        return "kakaoLoginSuccess";
    }
}
