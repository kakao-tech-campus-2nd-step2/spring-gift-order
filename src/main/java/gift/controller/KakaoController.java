package gift.controller;

import gift.service.kakao.AccessTokenResponse;
import gift.service.kakao.KakaoLoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/kakao")
@RestController
public class KakaoController {

    private final KakaoLoginService loginService;

    public KakaoController(KakaoLoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> redirectLoginForm() {
        HttpHeaders redirectHeaders = loginService.getRedirectHeaders();

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(redirectHeaders)
                .build();
    }

    @GetMapping("/oauth")
    public ResponseEntity<AccessTokenResponse> login(@RequestParam String code) {
        String accessToken = loginService.getAccessToken(code);
        AccessTokenResponse token = new AccessTokenResponse(accessToken);

        return ResponseEntity.ok()
                .body(token);
    }

}
