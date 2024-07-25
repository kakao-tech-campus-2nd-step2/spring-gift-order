package gift.controller;

import gift.KakaoProperties;
import gift.PasswordEncoder;
import gift.model.User;
import gift.service.KakaoService;
import gift.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class KakaoController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final UserService userService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService, UserService userService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @GetMapping("/kakao/login")
    public RedirectView kakaoLogin() {
        logger.info("qwer" + kakaoProperties.getClientId());
        String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri() + "&scope=account_email";
        return new RedirectView(url);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String authorizationCode, HttpSession session) {
        String accessToken = kakaoService.getAccessToken(authorizationCode);
        String email = kakaoService.getUserEmail(accessToken);

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("email", email);

        // 이메일을 로그로 출력
        logger.info("User email: " + email);
        logger.info("auth: " + accessToken);

        // 사용자 존재 여부 확인 및 생성
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User(null, email, PasswordEncoder.encode("1234"));
            userService.save(user);
        }

        return ResponseEntity.ok("Login successful");
    }
}