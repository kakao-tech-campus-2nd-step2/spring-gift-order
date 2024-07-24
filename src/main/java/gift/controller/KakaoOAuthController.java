package gift.controller;

import gift.auth.AuthService;
import gift.auth.KakaoAuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class KakaoOAuthController {

    private final AuthService kakaoAuthService;

    public KakaoOAuthController(@Qualifier("kakaoAuthService") AuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = kakaoAuthService.getLoginUrl();
        response.sendRedirect(kakaoLoginUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        Map<String, String> tokens = ((KakaoAuthService) kakaoAuthService).handleCallback(code);
        return ResponseEntity.ok(tokens);
    }
}
