package gift.controller;

import gift.service.KakaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KakaoLoginController {

    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakaoLogin")
    public String oauthLogin() {
        String url = "https://kauth.kakao.com/oauth/authorize?";
        url += "scope=talk_message&";
        url += "response_type=code&";
        url += "redirect_uri=http://localhost:8080&";
        url += "client_id=e8737f91343ed26716bff4973870e1b9";
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