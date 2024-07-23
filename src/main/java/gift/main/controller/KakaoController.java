package gift.main.controller;

import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoProfile;
import gift.main.dto.KakaoToken;
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
    private String code;
    private HttpServletResponse response;

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
    @GetMapping(value = "/callback", params = {"code"})
    public ResponseEntity<?> getKakaoCode(@RequestParam("code") String code) {
        System.out.println("1번");
        kakaoService.requestKakaoToken(code);
        return ResponseEntity.ok("Access token issuance successful");
    }

    //2. 전달받은 토큰으로 유저 정보 가져오기 -> 이건 왠지 가넝일듯
    @GetMapping("/callback")
    public ResponseEntity<?> getKakaoToken(HttpServletResponse response, @RequestBody String string) {
        System.out.println("2번");
        System.out.println("string = " + string);
        Map<String, Object> responseBody = new HashMap<>();
//        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(kakaoToken);
//        String token = userService.loginKakaoUser(kakaoProfile);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .body(responseBody);
        return null;
    }





}
