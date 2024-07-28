package gift.controller.kakao;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao/login")
public class KakaoViewController {

    private final KakaoProperties kakaoProperties;

    public KakaoViewController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping
    public String kakaoLogin(Model model) {
        model.addAttribute("kakao", kakaoProperties);
        return "kakaoLogin";
    }
}
