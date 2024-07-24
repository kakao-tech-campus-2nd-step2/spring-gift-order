package gift.controller;

import gift.dto.KakaoProperties;
import gift.service.KakaoService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.getClientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.getRedirectUrl());
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

        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        model.addAttribute("userInfo", userInfo);

        return "user";
    }

}
