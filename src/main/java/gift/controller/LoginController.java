package gift.controller;

import gift.util.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final KakaoProperties kakaoProperties;

    @Autowired
    public LoginController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUri", kakaoProperties.redirectUri());
        return "login";
    }
}
