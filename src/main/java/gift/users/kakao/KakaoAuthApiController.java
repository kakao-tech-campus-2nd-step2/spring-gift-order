package gift.users.kakao;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class KakaoAuthApiController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthApiController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/authorize")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = kakaoAuthService.getKakaoLoginUrl();
        response.sendRedirect(kakaoLoginUrl);
    }
}
