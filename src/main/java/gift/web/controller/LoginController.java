package gift.web.controller;

import gift.service.kakaoAuth.KakaoAuthService;
import gift.service.kakaoAuth.KakaoInfo;
import gift.service.member.MemberService;
import gift.web.dto.MemberDto;
import gift.web.dto.Token;
import gift.web.exception.MemberNotFoundException;
import gift.web.jwt.JwtUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final KakaoAuthService kakaoAuthService;
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public LoginController(KakaoAuthService kakaoAuthService, MemberService memberService,
        JwtUtils jwtUtils) {
        this.kakaoAuthService = kakaoAuthService;
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/login/kakao-auth")
    public String kakaoAuthConnect() {
        return "redirect:" + kakaoAuthService.getKakaoAuthUrl();
    }


    @GetMapping("/login/kakao-callback")
    public String kakaoAuthCallback(@RequestParam String code, RedirectAttributes rttr) {
        System.out.println(code);
        Token token = kakaoAuthService.receiveToken(code);

        System.out.println(token);
        KakaoInfo kakaoInfo = kakaoAuthService.getMemberInfoFromKakaoServer(token);

        MemberDto memberDto = null;

        try {
            memberDto = memberService.getMemberByEmail(kakaoInfo.email());
        } catch (MemberNotFoundException e) {
            rttr.addFlashAttribute("kakaoInfo", kakaoInfo);
            return "redirect:/register-social";
        }
        rttr.addAttribute("token", new Token(jwtUtils.createJWT(memberDto)));
        return "redirect:/";
    }

}
