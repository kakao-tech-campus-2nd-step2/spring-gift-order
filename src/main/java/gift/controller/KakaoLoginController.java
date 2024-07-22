package gift.controller;

import gift.DTO.KakaoProperties;
import gift.security.KakaoTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {
    private final KakaoTokenProvider kakaoTokenProvider;
    private final KakaoProperties kakaoProperties;

    public KakaoLoginController(KakaoTokenProvider kakaoTokenProvider, KakaoProperties kakaoProperties) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/code")
    public String getCode(@RequestParam String code){
        kakaoTokenProvider.getToken(code);
        return "/";
    }

    @GetMapping("/kakaoLogin")
    public String OauthLogin(){
        String url = "https://kauth.kakao.com/oauth/authorize?";
        url += "scope=talk_message&";
        url += "response_type=code&";
        url += "redirect_uri=" + kakaoProperties.getRedirectUrl() +"&";
        url += "client_id=" + kakaoProperties.getClientId();
        return "redirect:" + url;
    }
}
