package gift.controller;

import gift.service.MemberService;
import gift.util.KakaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/members")
public class MemberController {

    private static final Logger logger = Logger.getLogger(MemberController.class.getName());

    private final MemberService memberService;
    private final KakaoUtil kakaoUtil;

    @Value("${kakao.javascript-id}")
    private String kakaoJavaScriptKey;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    @Autowired
    public MemberController(MemberService memberService, KakaoUtil kakaoUtil) {
        this.memberService = memberService;
        this.kakaoUtil = kakaoUtil;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoJavaScriptKey", kakaoJavaScriptKey);
        model.addAttribute("redirectUri", redirectUri);
        return "login";
    }

    @GetMapping("/oauth/kakao")
    public String oauthKakao(@RequestParam String code, HttpSession session) {
        Map<String, Object> tokenResponse = kakaoUtil.getAccessToken(code);
        String accessToken = (String) tokenResponse.get("access_token");

        Map<String, Object> userInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoId = String.valueOf(userInfo.get("id"));
        String nickname = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");

        memberService.registerOrUpdateMember(kakaoId, nickname);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("kakaoId", kakaoId);

        // 세션에 저장된 값을 로그로 출력
        logger.info("AccessToken: " + session.getAttribute("accessToken"));

        return "redirect:/wishes/items";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/members/login";
    }
}
