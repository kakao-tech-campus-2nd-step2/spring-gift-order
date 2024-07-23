package gift.controller.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//code 발급용
@Controller
@RequestMapping("/admin/kakao")
@EnableConfigurationProperties(KakaoProperties.class)
public class AdminKakaoController {

    @Autowired
    private KakaoProperties kakaoProperties;


    @GetMapping
    public String kakaoLoginPage(Model model) {
        model.addAttribute("kakao", kakaoProperties);
        return "kakao"; // 템플릿 파일 kakao.html
    }

}
