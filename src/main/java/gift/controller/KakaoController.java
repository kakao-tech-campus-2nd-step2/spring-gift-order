package gift.controller;

import gift.service.KakaoProperties;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
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
    public String callback(@RequestParam String code, HttpServletRequest request) {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        // 사용자 정보 확인 및 처리
        String kakaoId = userInfo.get("id").toString();
        // 기존 회원인지 확인하고, 신규 회원이면 가입 처리
        // 예를 들어, 사용자 정보를 데이터베이스에 저장하는 로직을 추가할 수 있습니다.

        // 액세스 토큰을 세션에 저장
        request.getSession().setAttribute("accessToken", accessToken);

        // 로그인 완료 후 홈 페이지로 리디렉션
        return "redirect:/home";
    }

    private String removeNewlines(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[\\r\\n]", ""); // 개행 문자 제거
    }
}