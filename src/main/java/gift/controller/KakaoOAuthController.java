package gift.controller;

import gift.dto.KakaoUserProfile;
import gift.service.KakaoOAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoService;

    public KakaoOAuthController(KakaoOAuthService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoService.getAccessToken(code);
        KakaoUserProfile userProfile = kakaoService.getUserProfile(accessToken);
        return "redirect:/";
    }
}
