package gift.controller;

import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/kakao")
public class KakaoAuthController {
    private KakaoService kakaoService;
    public KakaoAuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public RedirectView getKakaoLoginUrl() {
        RedirectView redirectView = new RedirectView();
        String url = kakaoService.getKakaoUrl();
        redirectView.setUrl(url);
        return redirectView;
    }
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }
}
