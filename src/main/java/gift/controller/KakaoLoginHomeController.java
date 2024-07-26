package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KakaoLoginHomeController {
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.scope}")
    private String scope;

    @GetMapping("/kakao/login/home")
    public String kakaoLoginPage(Model model) {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + scope;
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
        return "kakao-login-home";
    }
}
