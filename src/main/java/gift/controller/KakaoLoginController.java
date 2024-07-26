package gift.controller;

import gift.service.KakaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakaoLogin")
    public String oauthLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?";
        url += "scope=talk_message&";
        url += "response_type=code&";
        url += "redirect_uri=" + redirectUri + "&";
        url += "client_id=" + clientId;
        return "redirect:" + url;
    }

    @GetMapping("/")
    public RedirectView callback(@RequestParam(name = "code") String code, RedirectAttributes redirectAttributes) throws Exception {
        String token = kakaoService.login(code);
        redirectAttributes.addFlashAttribute("token", token);
        return new RedirectView("/home");
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
}