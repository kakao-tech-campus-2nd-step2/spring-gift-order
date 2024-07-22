package gift.main.controller;

import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoTokenResponse;
import gift.main.dto.UserJoinRequest;
import gift.main.service.KakaoService;
import gift.main.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, UserService userService, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.kakaoService = kakaoService;
    }

    //카카오 로그인 화면 보여주기
    @GetMapping("")
    public void requestKakaoLoginScreen(HttpServletResponse response) throws IOException {
        String url = kakaoProperties.getCodeRequestUri() +
                "?client_id=" + kakaoProperties.getClientId() +
                "&redirect_uri=" + kakaoProperties.getRedirectUri() +
                "&response_type=code";

        response.sendRedirect(url);
    }

    //카카오 로그인+유저정보등록+jwt발급하기
    @GetMapping("/login")
    public String getKakaoCode(@RequestParam("code") String code, HttpServletResponse response) {
        KakaoTokenResponse tokenResponse = kakaoService.receiveKakaoToken(code);
        UserJoinRequest userJoinRequest = kakaoService.getKakaoProfile(tokenResponse);
        String token = userService.loginKakaoUser(userJoinRequest);

        return token;
    }

}
