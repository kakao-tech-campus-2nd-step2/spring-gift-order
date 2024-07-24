package gift.controller;

import gift.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_url}")
    private String redirectUrl;

    @GetMapping("/login")
    public ResponseEntity<Void> kakaoLogin() {
        String uri = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
                + "&redirect_uri=" + redirectUrl
                + "&response_type=code";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uri));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/getauth")
    public ResponseEntity<String> getAuth(@RequestParam("code") String code) {
        String accessToken = kakaoLoginService.getToken(code);
        return ResponseEntity.ok("토큰을 받았습니다.");
    }
}
