package gift.product.controller;

import gift.product.service.KakaoService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class ApiKakaoController {

    private final KakaoService kakaoService;

    public ApiKakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    // authorize code는 KakaoController에서 받아와야 함.

    // 반환되는 토큰은 Kakao API의 access token이 아닌 나의 어플리케이션 Jwt token
    @GetMapping(value = "/callback", params = "code")
    public ResponseEntity<String> login(@RequestParam String code) {
        System.out.println("[ApiKakaoController] login()");
        return ResponseEntity.status(HttpStatus.OK).body("token: " + kakaoService.login(code));
    }
}
