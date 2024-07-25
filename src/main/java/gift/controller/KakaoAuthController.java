package gift.controller;

import gift.dto.KakaoTokenResponseDTO;
import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class KakaoAuthController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<KakaoTokenResponseDTO> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponseDTO tokenResponse = kakaoAuthService.getKakaoToken(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
