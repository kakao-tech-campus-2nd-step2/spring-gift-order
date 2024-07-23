package gift.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KakaoLoginController {

    @Value("${kakao.api_key}")
    private String kakaoApikey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;


    @GetMapping("/login")
    public String login(Model model){
        System.out.println(kakaoApikey + " " + kakaoRedirectUri);
        model.addAttribute("kakaoApiKey", kakaoApikey);
        model.addAttribute("redirectUri", kakaoRedirectUri);
        return "login";
    }
    
}
