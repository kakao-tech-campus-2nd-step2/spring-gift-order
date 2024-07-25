package gift.controller;

import gift.dto.KakaoTokenResponseDTO;
import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class KakaoAuthController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login/kakao")
    public void redirectKakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<KakaoTokenResponseDTO> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponseDTO tokenResponse = kakaoAuthService.getKakaoToken(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
