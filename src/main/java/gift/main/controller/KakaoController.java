package gift.main.controller;

import gift.main.Exception.CustomException;
import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoProfileRequest;
import gift.main.dto.KakaoToken;
import gift.main.entity.User;
import gift.main.service.KakaoService;
import gift.main.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kakao/login")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, UserService userService, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.kakaoService = kakaoService;
    }

    //0. 카카오 로그인 화면 보여주기
    @GetMapping()
    public void requestKakaoLoginScreen(HttpServletResponse response) throws IOException {
        String url = kakaoProperties.codeRequestUri() +
                "?client_id=" + kakaoProperties.clientId() +
                "&redirect_uri=" + kakaoProperties.redirectUri() +
                "&response_type=code";
        response.sendRedirect(url);
    }

    //1. 전달받은 코드로 엑세스 토큰 요청하기
    @GetMapping("/callback")
    public ResponseEntity<?> LoginKakaoUser(@RequestParam(value = "code",required = false) String code,
                                          @RequestParam(value = "error",required = false) String error,
                                          @RequestParam(value = "error_description",required = false) String errorDescription) {
        if (error!=null || errorDescription!=null ) {
            throw new CustomException(HttpStatus.BAD_REQUEST, errorDescription);
        }

        KakaoToken kakaoToken = kakaoService.requestKakaoToken(code);
        KakaoProfileRequest kakaoProfile = kakaoService.getKakaoProfile(kakaoToken);
        Map<String, Object> map = userService.loginKakaoUser(kakaoProfile);

        String token = (String) map.get("token");
        User saveUser = (User) map.get("user");

        kakaoService.saveToken(saveUser, kakaoToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body("");
    }






}
