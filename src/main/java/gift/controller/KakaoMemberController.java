package gift.controller;

import gift.dto.request.MemberRequest;
import gift.service.KakaoMemberService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoMemberController {
    private final KakaoMemberService kakaoMemberService;
    private final MemberService memberService;

    public KakaoMemberController(KakaoMemberService kakaoMemberService, MemberService memberService) {
        this.kakaoMemberService = kakaoMemberService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "members/login";
    }

    @GetMapping("/members/login/kakao/oauth")
    public String kakaoLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token!=null && kakaoMemberService.isKakaoTokenValid(token.substring(7))){
            return "redirect:/members/wishes";
        }
        return "redirect:" + kakaoMemberService.getKakaoCodeUrl();
    }

    @GetMapping("/members/login/kakao/callback")
    public String registerKakaoUser(@RequestParam("code") String code) {
        String token = kakaoMemberService.getKakaoToken(code);
        String userEmail = kakaoMemberService.getKakaoUserEmail(token);
        memberService.save(new MemberRequest(null,userEmail,null));
        return "redirect:/members/wishes";
    }

    @GetMapping("/members/wishes")
    public String showWishlist() {
        return "wishes/list";
    }
}