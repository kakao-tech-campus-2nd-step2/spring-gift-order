package gift.controller;

import gift.dto.request.MemberRequest;
import gift.service.KakaoService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KakaoMemberController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoMemberController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "members/login";
    }

    @GetMapping("/members/login/kakao/oauth")
    public String kakaoLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token!=null && kakaoService.isKakaoTokenValid(token.substring(7))){
            return "redirect:/members/wishes";
        }
        return "redirect:" + kakaoService.getKakaoCodeUrl();
    }

    @GetMapping("/members/login/kakao/callback")
    public String registerKakaoUser(HttpServletRequest servletRequest) {
        String code = servletRequest.getParameter("code");
        String token = kakaoService.getKakaoToken(code);
        String userEmail = kakaoService.getKakaoUserEmail(token);
        memberService.save(new MemberRequest(null,userEmail,null));
        return "redirect:/members/wishes";
    }

    @GetMapping("/members/wishes")
    public String showWishlist() {
        return "wishes/list";
    }
}