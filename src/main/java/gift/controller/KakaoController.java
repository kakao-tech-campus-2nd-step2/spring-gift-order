package gift.controller;

import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoController {
    @Value("${kakao.client-id}")
    String clientId;
    @Value("${kakao.redirect-url}")
    String redirectUrl;

    private final KakaoAuthService kakaoAuthService;

    public KakaoController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping(value="/kakao")
    public String kakaoConnect() {
        System.out.println("redirect_uri=" + redirectUrl);
        System.out.println("client_id=" + clientId);
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&");
        url.append("redirect_uri=" + redirectUrl);
        url.append("&client_id=" + clientId);
        return "redirect:" + url.toString();
    }

    @GetMapping("/kakaoAuth")
    public String getKakaoAuthToken(@RequestParam String code){
        String token = kakaoAuthService.getKakaoToken(code);
        System.out.println(token);
        return "redirect:/products";
    }
}
