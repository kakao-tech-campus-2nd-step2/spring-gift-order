package gift.controller;

import gift.domain.model.dto.TokenResponseDto;
import gift.service.KakaoLoginService;
import gift.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final UserService userService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, UserService userService) {
        this.kakaoLoginService = kakaoLoginService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<TokenResponseDto> callback(@RequestParam("code") String code) {
        String accessToken = kakaoLoginService.getAccessTokenFromKakao(code);
        TokenResponseDto tokenResponse = userService.loginOrRegisterKakaoUser(accessToken);
        return ResponseEntity.ok(tokenResponse);
    }
}