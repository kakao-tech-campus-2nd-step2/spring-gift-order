package gift.domain.user.controller;

import gift.auth.jwt.JwtToken;
import gift.domain.user.service.OauthLoginService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/login")
public class OauthLoginController {

    private final OauthLoginService oauthLoginService;

    public OauthLoginController(OauthLoginService oauthLoginService) {
        this.oauthLoginService = oauthLoginService;
    }

    @GetMapping("/kakao")
    public void getAuthCodeUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(oauthLoginService.getAuthCodeUrl());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<JwtToken> login(@RequestParam("code") String code) {
        JwtToken jwtToken = oauthLoginService.login(code);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
