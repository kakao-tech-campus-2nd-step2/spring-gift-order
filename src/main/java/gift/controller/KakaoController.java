package gift.controller;

import gift.dto.KakaoProperties;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpSession;
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
    public String kakaoCallback(@RequestParam(required = false) String code, Model model,
        HttpSession session) {
        if (code == null) {
            throw new IllegalArgumentException("Authorization code가 없습니다.");
        }

        Map<String, Object> tokenResponse = kakaoService.getAccessToken(code);
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new RuntimeException("잘못된 access token");
        }
        String accessToken = (String) tokenResponse.get("access_token");

        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        session.setAttribute("userInfo", userInfo);
        session.setAttribute("accessToken", accessToken);

        System.out.println("Access Token: " + accessToken);

        return "redirect:/user";
    }

    @GetMapping("/user")
    public String user(Model model, HttpSession session) {
        Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute("userInfo");

        if (userInfo == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다.");
        }

        model.addAttribute("userInfo", userInfo);
        return "user";
    }

}
