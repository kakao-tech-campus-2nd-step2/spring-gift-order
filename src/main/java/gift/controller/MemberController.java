package gift.controller;

import gift.dto.request.MemberRequest;
import gift.service.KakaoMemberService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class MemberController {
    private final KakaoMemberService kakaoMemberService;
    private final MemberService memberService;

    public MemberController(KakaoMemberService kakaoMemberService, MemberService memberService) {
        this.kakaoMemberService = kakaoMemberService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "members/login";
    }

    @GetMapping("/kakao/login")
    public String kakaoLogin(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .map(kakaoMemberService::processToken)
                .filter(kakaoMemberService::checkTokenExists)
                .map(token -> "redirect:/wishes")
                .orElseGet(() -> "redirect:" + kakaoMemberService.getKakaoCodeUrl());
    }

    @GetMapping
    public String getKakaoToken(@RequestParam("code") String code, HttpSession session) {
        String token = kakaoMemberService.getKakaoToken(code);
        session.setAttribute("accessToken", token);
        return "redirect:/kakao/register";
    }

    @GetMapping("/kakao/register")
    public String showKakaoRegisterForm(HttpSession session, Model model) {
        String token = (String) session.getAttribute("accessToken");
        if (token == null) {
            return "redirect:/kakao/login";
        }
        model.addAttribute("accessToken", token);
        model.addAttribute("memberDto", new MemberRequest(null, null, null));
        return "members/kakaoRegister";
    }

    @PostMapping("/kakao/register")
    public String addKakaoMember(@ModelAttribute MemberRequest memberRequest, @RequestParam("accessToken") String accessToken) {
        memberService.save(memberRequest);
        kakaoMemberService.save(accessToken, memberRequest.email());
        return "redirect:/wishes";
    }

    @GetMapping("/wishes")
    public String showWishlist() {
        return "wishes/list";
    }
}
