package gift.auth.controller;

import gift.config.KakaoOauthConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoOauthController {
    private final KakaoOauthConfig kakaoOauthConfig;

    public KakaoOauthController(KakaoOauthConfig kakaoOauthConfig) {
        System.out.println(kakaoOauthConfig);
        this.kakaoOauthConfig = kakaoOauthConfig;
    }

    @GetMapping
    public ResponseEntity<Void> getOauthURL() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("location", kakaoOauthConfig.createURL())
                .build();
    }
}
