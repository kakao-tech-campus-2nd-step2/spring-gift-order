package gift.doamin.user.controller;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import gift.doamin.user.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }


    @GetMapping("/oauth2/authorization/kakao")
    public ModelAndView kakaoLogin(HttpServletResponse response) {

        return new ModelAndView("redirect:" + oAuthService.getAuthUrl());
    }

    @GetMapping("login/oauth2/code/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam(name = "code") String authorizeCode) {
        KakaoOAuthTokenResponse kakaoOAuthToken = oAuthService.requestToken(authorizeCode);
        String myRefreshToken = oAuthService.authenticate(kakaoOAuthToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", myRefreshToken)
            .httpOnly(true)
            .maxAge(12*60*60)
            .path("/api/auth/accessToken")
            .build();
        return ResponseEntity.ok()
            .header("Set-Cookie", cookie.toString())
            .build();
    }

}
