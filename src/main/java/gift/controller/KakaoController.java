package gift.controller;

import gift.auth.KakaoProperties;
import gift.domain.Token;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao/login")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String codeUrl = kakaoService.getCode();
        response.sendRedirect(codeUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> loginByKakaoEmail(@RequestParam("code") String code) {
       Token token = kakaoService.login(code);
       return ResponseEntity.ok(token);
    }

}

