package gift.controller;

import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("kakaoClientId", kakaoService.getKakaoClientId());
        model.addAttribute("kakaoRedirectUrl", kakaoService.getKakaoRedirectUrl());
        return "login";
    }

    @GetMapping("/direct/login")
    public String loginForm() {
        return "redirect:" + kakaoService.getKakaoLogin();
    }

    @GetMapping("/")
    public String kakaoCallback(@RequestParam(required = false) String code, Model model) {
        if (code == null) {
            return "redirect:/login?error";
        }

        String accessToken = kakaoService.getAccessToken(code);
        if (accessToken == null) {
            return "redirect:/login?error";
        }

        model.addAttribute("token", accessToken);
        return "user";
    }

}
