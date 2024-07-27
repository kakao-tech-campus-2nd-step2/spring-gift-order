package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KakaoLoginController {

    @GetMapping("/kakao/login")
    public String login() {
        return "kakaoLogin";
    }
}
