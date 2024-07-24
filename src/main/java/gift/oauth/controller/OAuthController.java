package gift.oauth.controller;

import gift.oauth.exception.KakaoOAuthException;
import gift.oauth.service.OAuthService;
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

    private final OAuthService  oauthService;

    public OAuthController(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/authorize")
    public void getAuthorizeUri(HttpServletResponse response) throws IOException {
        response.sendRedirect(oauthService.getAuthorizeUri());
    }

    @GetMapping("/token")
    public ResponseEntity<String> getAccessToken(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String error_description
    ){
        if (error != null || error_description != null){
            throw new KakaoOAuthException(error, error_description);
        }
        return ResponseEntity.ok(oauthService.getAccessToken(code));
    }
}
