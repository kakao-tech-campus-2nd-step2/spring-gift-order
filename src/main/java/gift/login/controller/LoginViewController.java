package gift.login.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginViewController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @GetMapping
    public String showLoginPage(Model model){
        String location = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
            + "&redirect_url=" + redirectUrl
            + "&response_type=code";
        model.addAttribute("location", location);

        return "kakao_login";
    }
}
