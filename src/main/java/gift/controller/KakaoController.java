package gift.controller;

import gift.config.KakaoProperties;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

@Controller
public class KakaoController {

    @Autowired
    private KakaoProperties kakaoProperties;

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri();
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code) {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        // 사용자 정보 확인 및 처리
        // 예: 사용자가 기존 회원인지 확인하고, 신규 회원이면 가입 처리
        // 로그인 완료 후 홈 페이지로 리디렉션
        return "redirect:/home";
    }
}