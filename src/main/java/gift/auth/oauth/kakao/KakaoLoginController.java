package gift.auth.oauth.kakao;

import gift.auth.dto.Token;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/login")
    public void getAuthCodeUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoLoginService.getAuthCodeUrl());
    }

    @GetMapping("/login/callback")
    public ResponseEntity<Token> login(@RequestParam("code") String code) {
        Token token = kakaoLoginService.login(code);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
