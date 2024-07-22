package gift.product.controller;

import gift.product.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/kakao/oauth")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/authorize")
    public String getAuthCode() {
        return "redirect:" + kakaoService.getAuthCode();
    }
}
