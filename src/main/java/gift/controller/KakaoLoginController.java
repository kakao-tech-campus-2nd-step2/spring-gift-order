package gift.controller;

import gift.model.member.KakaoProperties;
import gift.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/login")
    public String kakaoLogin() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoProperties.getClientId());
        url.append("&redirect_uri="+kakaoProperties.getRedirectUri());
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        return ResponseEntity.ok("Access Token: " + accessToken);
    }
}
