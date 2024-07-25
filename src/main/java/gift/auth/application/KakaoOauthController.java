package gift.auth.application;

import gift.auth.service.KakaoOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOauthController {
    private final KakaoOAuthService kakaoOAuthService;

    public KakaoOauthController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping
    public ResponseEntity<Void> getOauthURL() {
        var kakaoLoginUrl = kakaoOAuthService.getKakaoLoginUrl();
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("location", kakaoLoginUrl)
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callBack(@RequestParam("code") String code) {
        kakaoOAuthService.callBack(code);
//        kakaoOAuthService.(code);
        return ResponseEntity.ok()
                .body(code);
    }
}
