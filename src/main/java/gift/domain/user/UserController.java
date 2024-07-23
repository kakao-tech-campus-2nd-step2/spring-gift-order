package gift.domain.user;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Value("${kakao.client.id}")
    private String clientId; // 카카오 개발자 콘솔에서 발급받은 클라이언트 ID

    @Value("${kakao.redirect.url}")
    private String redirectUri; // 카카오 로그인 후 리다이렉트될 URI

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String RESPONSE_TYPE = "code"; // 기본적으로 "code"로 설정

    @GetMapping("/login")
    public String loginPage(Model model) {
        String url = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s",
            KAKAO_AUTH_URL, clientId, redirectUri, RESPONSE_TYPE);

        model.addAttribute("kakaoLoginUrl", url);
        return "login";
    }

}
