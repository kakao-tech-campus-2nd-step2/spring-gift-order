package gift.controller;

import gift.domain.Member;
import gift.service.KakaoService;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class KakaoController {
    KakaoService kakaoService;
    MemberService memberService;
    KakaoTokenService kakaoTokenService;

    public KakaoController(KakaoService kakaoService, MemberService memberService, KakaoTokenService kakaoTokenService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @GetMapping("/")
    public ResponseEntity<String> handleRequest(@RequestParam(name = "code", required = false) String code) {
        String token = kakaoService.getToken(code);
        String userId = kakaoService.getUserInfo(token);
        Member member = new Member(userId + "@kakao.com", "kakao");
        memberService.register(member);
        kakaoTokenService.saveKakaoToken(userId + "@kakao.com", token);

        return ResponseEntity.ok("User registered and Kakao token saved successfully");
    }

    @GetMapping("/login/kakao")
    public String login(Model model) {
        model.addAttribute("client_id", kakaoService.getClientId());
        model.addAttribute("redirect_uri", kakaoService.getRedirectUri());
        return "kakaoLogin";
    }
}
