package gift.controller;

import gift.entity.Member;
import gift.exception.MemberNotFoundException;
import gift.service.KakaoService;
import gift.util.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    @Autowired
    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/login")
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.redirectUrl());
        return "kakaoLogin";
    }

    @GetMapping("/kakao/oauth2/callback")
    public String callbackKakao(@RequestParam String code, Model model) {

        try {
            String accessToken = kakaoService.login(code);
            return "home";
        } catch (MemberNotFoundException e) {
            model.addAttribute("member", new Member()); // Member 객체 추가
            return "register";
        }

    }

    @GetMapping("/kakao/loginSuccess")
    public String loginSuccess() {
        return "kakaoLoginSuccess";
    }


}
