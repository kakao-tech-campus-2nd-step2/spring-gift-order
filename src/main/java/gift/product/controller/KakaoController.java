package gift.product.controller;

import gift.product.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public String getAuthCode() {
        return "redirect:" + kakaoService.getAuthCode();
    }

    @GetMapping(params = "code")
    public String login(@RequestParam String code) {
        kakaoService.getAccessToken(code);
        return "product-list";
    }
}
