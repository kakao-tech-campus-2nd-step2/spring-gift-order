package gift.controller;


import gift.config.KakaoProperties;
import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/auth")
    public String kakaoLogin(Model model) {
        model.addAttribute("clientId", kakaoProperties.clientId());
        model.addAttribute("redirectUrl", kakaoProperties.redirectUrl());
        return "kakao_login";
    }

    @GetMapping("/")
    public String kakaoAccessToken(@RequestParam(value = "code") String authorizationCode, RedirectAttributes redirectAttributes) {
        if (authorizationCode != null) {
            String accessToken = kakaoService.getAccessToken(authorizationCode);
            redirectAttributes.addFlashAttribute("accessToken", accessToken);
            return "redirect:/kakao/success";
        }
        return "kakao_login";
    }

    @GetMapping("/kakao/success")
    public String kakaoAcessSuccess(Model model) {
        return "kakao_access_token";
    }
}