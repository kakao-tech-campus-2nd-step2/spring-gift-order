package gift.controller;

import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final KakaoAuthService kakaoAuthService;

    @Autowired
    public KakaoOAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = kakaoAuthService.getKakaoLoginUrl();
        response.sendRedirect(kakaoLoginUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        Map<String, String> tokens = kakaoAuthService.handleKakaoCallback(code);
        return ResponseEntity.ok(tokens);
    }
}
