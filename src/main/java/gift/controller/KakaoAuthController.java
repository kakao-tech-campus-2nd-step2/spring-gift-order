package gift.controller;

import gift.config.KakaoProperties;
import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/oauth")
public class KakaoAuthController {

    private final KakaoProperties kakaoProperties;
    private final KakaoAuthService kakaoAuthService;

    @Autowired
    public KakaoAuthController(KakaoProperties kakaoProperties, KakaoAuthService kakaoAuthService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/kakao")
    public ModelAndView redirectToKakaoLogin() {
        String url = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        return new ModelAndView("redirect:" + url);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> getAccessToken(@RequestParam String code) {
        String token = kakaoAuthService.getAccessToken(code);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}