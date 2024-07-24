package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class KakaoLoginHomeController {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/kakao/login/home")
    public String kakaoLoginPage(Model model) {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
        return "kakao-login-home";
    }
}
