package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginForm(Model model) {
        model.addAttribute("kakaoApiKey", "5bc075cfab5ae4b53f2d3eb6e44ed01f");
        model.addAttribute("redirectUri", "http://localhost:8080/login/code/kakao");
        return "login";
    }


}
