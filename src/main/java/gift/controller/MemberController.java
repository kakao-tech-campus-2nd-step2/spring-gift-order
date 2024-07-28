package gift.controller;

import gift.service.MemberService;
import gift.util.KakaoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member Management", description = "회원 관리 API")
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

    @Operation(summary = "로그인 폼 조회", description = "로그인 폼을 조회합니다.")
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoJavaScriptKey", kakaoJavaScriptKey);
        model.addAttribute("redirectUri", redirectUri);
        return "login";
    }

    @Operation(summary = "카카오 로그인 처리", description = "카카오 로그인을 처리합니다.")
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

    @Operation(summary = "로그아웃 처리", description = "로그아웃을 처리합니다.")
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/members/login";
    }
}
