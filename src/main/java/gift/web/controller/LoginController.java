package gift.web.controller;

import gift.service.kakaoAuth.KakaoAuthService;
import gift.service.member.MemberService;
import gift.web.jwt.JwtUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {

    private final KakaoAuthService kakaoAuthService;

    public LoginController(KakaoAuthService kakaoAuthService, MemberService memberService,
        JwtUtils jwtUtils) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/login/kakao-auth")
    public String kakaoAuthConnect() {
        return "redirect:" + kakaoAuthService.getKakaoAuthUrl();
    }
}
