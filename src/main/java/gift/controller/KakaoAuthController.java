package gift.controller;

import gift.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private KakaoLoginService kakaoLoginService;
    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/login")
    public RedirectView getKakaoLoginUrl() {
        RedirectView redirectView = new RedirectView();
        String url = kakaoLoginService.getKakaoUrl();
        redirectView.setUrl(url);
        return redirectView;
    }
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        return ResponseEntity.ok(accessToken);
    }
}
