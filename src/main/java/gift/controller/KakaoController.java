package gift.controller;

import gift.domain.Member;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class KakaoController {
    KakaoService kakaoService;
    MemberService memberServic;

    public KakaoController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberServic = memberService;
    }

    @GetMapping("/")
    public ResponseEntity<String> handleRequest(@RequestParam(name = "code", required = false) String code) {
        String Token = kakaoService.getToken(code);
        String userId = kakaoService.getUserInfo(Token);
        Member member = new Member(userId+"@kakao.com","kakao");
        memberServic.register(member);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/login/kakao")
    public String login(Model model) {
        model.addAttribute("client_id", kakaoService.getClientId());
        model.addAttribute("redirect_uri", kakaoService.getRedirectUri());
        return "kakaoLogin";
    }
}
