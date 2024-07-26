package gift.oauth.controller;

import gift.kakaoApi.exceptiion.KakaoLoginException;
import gift.kakaoApi.service.KakaoApiService;
import gift.oauth.service.OAuthService;
import gift.util.dto.JwtResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthService oauthService;
    private final KakaoApiService kakaoApiService;

    public OAuthController(OAuthService oauthService, KakaoApiService kakaoApiService) {
        this.oauthService = oauthService;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/login")
    public void loginKakao(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoApiService.getKakaoLoginUri());
    }

    @GetMapping("/token")
    public ResponseEntity<JwtResponse> getAccessToken(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String error_description
    ) {
        if (error != null || error_description != null) {
            throw new KakaoLoginException(error, error_description);
        }
        return ResponseEntity.ok(new JwtResponse(oauthService.getAccessToken(code)));
    }
}
