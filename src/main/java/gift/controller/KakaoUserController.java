package gift.controller;

//import gift.service.KakaoApiService;
import gift.service.KakaoUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoUserController {
    private KakaoUserService kakaoUserService;
//    private KakaoApiService kakaoApiService;

    @Autowired
    public KakaoUserController(KakaoUserService kakaoUserService) {
        this.kakaoUserService = kakaoUserService;
    }

    @GetMapping("/authorize")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = kakaoUserService.getAuthorizeUrl();
        response.sendRedirect(kakaoAuthUrl);
    }


    @GetMapping("/token")
    public ResponseEntity<String> getTokenGET(@RequestParam String code) {
        String token = kakaoUserService.getAccessToken(code);
//        kakaoApiService.setKakaoApiToken(token);  // 토큰을 설정합니다.
        return ResponseEntity.ok(token);
    }
}
