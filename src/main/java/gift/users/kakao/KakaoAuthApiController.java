package gift.users.kakao;

import gift.error.KakaoAuthenticationException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class KakaoAuthApiController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthApiController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/authorize")
    public void kakaoLogin(HttpServletResponse response) {
        String kakaoLoginUrl = kakaoAuthService.getKakaoLoginUrl();
        try {
            response.sendRedirect(kakaoLoginUrl);
        } catch (IOException e){
            throw new KakaoAuthenticationException("카카오 로그인에 실패했습니다.");
        }
    }

    @GetMapping("/token")
    public ResponseEntity<String> kakaoCallBack(@RequestParam("code") String code) {
        String jwtToken = kakaoAuthService.kakaoCallBack(code);
        return ResponseEntity.ok(jwtToken);
    }
}
