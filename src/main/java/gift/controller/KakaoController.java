package gift.controller;

import gift.model.BearerToken;
import gift.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("kakoAuth return : " + token);
        return "redirect:/products";
    }

    @GetMapping("/KakaoOrder/{productId}/{optionName}/{num}")
    @ResponseBody
    public void orderProduct(HttpServletRequest request, @PathVariable Long productId,
                             @PathVariable String optionName, @PathVariable int num){
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        kakaoAuthService.orderProduct(token.getToken(), productId, optionName, num);
    }
}
