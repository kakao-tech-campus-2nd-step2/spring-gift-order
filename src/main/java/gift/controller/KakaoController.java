package gift.controller;

import gift.service.KakaoProperties;
import gift.service.KakaoService;
import gift.value.KakaoString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String clientId = new KakaoString(kakaoProperties.getClientId()).removeNewlines();
        String redirectUri = new KakaoString(kakaoProperties.getRedirectUri()).removeNewlines();
        String authUrl = new KakaoString(kakaoProperties.getAuthUrl()).removeNewlines();
        String url = authUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletRequest request) {
        String accessToken = kakaoService.getAccessToken(code);

        // 액세스 토큰을 세션에 저장
        request.getSession().setAttribute("accessToken", accessToken);

        // 로그인 완료 후 홈 페이지로 리디렉션
        return "redirect:/home";
    }
}