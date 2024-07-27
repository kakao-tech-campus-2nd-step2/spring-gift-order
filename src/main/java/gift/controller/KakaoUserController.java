package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserResponse;
import gift.service.KakaoUserService;
import gift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;
    private final UserService userService;
    private final KakaoProperties kakaoProperties;

    public KakaoUserController(KakaoUserService kakaoUserService, UserService userService,
        KakaoProperties kakaoProperties) {
        this.kakaoUserService = kakaoUserService;
        this.userService = userService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/kakao/auth")
    public RedirectView redirectToAuthorization() {
        String url = kakaoUserService.getAuthorizationUrl();
        return new RedirectView(url);
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<KakaoTokenResponse> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse response = kakaoUserService.getAccessToken(code);

        kakaoProperties.setAccessToken(response.accessToken());

        KakaoUserResponse userResponse = kakaoUserService.getUserInfo(
            kakaoProperties.getAccessToken());
        userService.kakaoUserRegister(userResponse.kakaoAccount().email(),
            kakaoProperties.getDefaultPassword());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/kakao/userinfo")
    public ResponseEntity<KakaoUserResponse> kakaoUserInfo(@RequestParam String accessToken) {
        return ResponseEntity.ok().body(kakaoUserService.getUserInfo(accessToken));
    }

}
