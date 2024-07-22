package gift.kakaoLogin;

import gift.user.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String REST_API_KEY;

    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/login")
    public String getLoginView(Model model){
        model.addAttribute("redirect_url", REDIRECT_URI);
        model.addAttribute("client_id", REST_API_KEY);
        model.addAttribute("loginDTO", new LoginDTO());
        return "Login";
    }

    @GetMapping("/")
    public String getCode(@RequestParam String code){
        System.out.println(code);
        String accessToken = kakaoLoginService.login(code);
        return "MainView";
    }



}
