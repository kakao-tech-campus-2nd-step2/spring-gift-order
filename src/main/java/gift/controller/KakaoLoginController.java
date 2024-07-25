package gift.controller;

import gift.DTO.KakaoToken;
import gift.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/login/kakao")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService KakaoLoginService;

    @GetMapping
    public void getCode(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = KakaoLoginService.getAuthorizeUrl();
        response.sendRedirect(kakaoAuthUrl);
    }

    @GetMapping("/token")
    public ResponseEntity<KakaoToken> getTokenGET(@RequestParam("code") String code) {
        var token = KakaoLoginService.getToken(code);
//        var ret = KakaoLoginService.getKakaoUserInfo(token.access_token());
        return ResponseEntity.ok(token);
    }
}
