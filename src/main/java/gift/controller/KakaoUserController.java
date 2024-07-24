package gift.controller;

import gift.dto.KakaoTokenResponse;
import gift.service.KakaoUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;

    public KakaoUserController(KakaoUserService kakaoUserService) {
        this.kakaoUserService = kakaoUserService;
    }

    @GetMapping("/kakao/auth")
    public RedirectView redirectToAuthorization() {
        String url = kakaoUserService.getAuthorizationUrl();
        return new RedirectView(url);
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<KakaoTokenResponse> KakaoCallback(@RequestParam String code) {
        return ResponseEntity.ok().body(kakaoUserService.getAccessToken(code));
    }

}
