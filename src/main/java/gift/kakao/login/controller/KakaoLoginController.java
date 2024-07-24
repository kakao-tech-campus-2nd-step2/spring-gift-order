package gift.kakao.login.controller;

import gift.kakao.login.service.KakaoLoginService;
import gift.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("")
    public ResponseEntity<String> handleOAuthCallback(
        @RequestParam(value = "code") String code) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        String kakaoEmail = kakaoLoginService.getUserInfo(accessToken);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
}
