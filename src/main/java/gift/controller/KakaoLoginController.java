package gift.controller;

import gift.auth.DTO.TokenDTO;
import gift.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public TokenDTO getTokenGET(@RequestParam("code") String code) {
        var kakaoToken = KakaoLoginService.getToken(code);
        return KakaoLoginService.createToken(kakaoToken);
    }
}
