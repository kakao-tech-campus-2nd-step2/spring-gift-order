package gift.controller;

import gift.domain.KakaoProperties;
import gift.domain.KakaoTokenResponsed;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public void redirectKaKaoLogin(HttpServletResponse response) throws IOException {
        String url = kakaoProperties.authUrl() +
            "?response_type=code&client_id=" + kakaoProperties.clientId() +
            "&redirect_uri=" + kakaoProperties.redirectUrl();
        response.sendRedirect(url);
    }

//    @GetMapping("/token")
//    public ResponseEntity<String> getAccessToken(@RequestParam String code){
//        String token = kakaoService.getAccessToken(code);
//        return new ResponseEntity<>(token, HttpStatus.OK);
//    }

    @GetMapping("/token")
    public ResponseEntity<KakaoTokenResponsed> getAccessToken(@RequestParam String code){
        KakaoTokenResponsed token = kakaoService.getTokeResponse(code);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }



}
