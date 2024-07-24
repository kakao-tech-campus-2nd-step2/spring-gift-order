package gift.controller;

import gift.model.member.KakaoProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;

    public KakaoLoginController(KakaoProperties kakaoProperties){
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/login")
    public String kakaoLogin() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoProperties.getClientId());
        url.append("&redirect_uri="+kakaoProperties.getRedirectUrl());
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }
}
