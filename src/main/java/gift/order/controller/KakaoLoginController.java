package gift.order.controller;

import gift.common.config.KakaoProperties;
import gift.common.util.CommonResponse;
import gift.order.dto.KakaoUser;
import gift.order.dto.KakaoTokenResponse;
import gift.order.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/kakao/login")
public class KakaoLoginController {
    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    public KakaoLoginController(KakaoProperties properties, KakaoService kakaoService) {
        this.kakaoProperties = properties;
        this.kakaoService = kakaoService;
    }

    // 1. 카카오 로그인을 통해 인가 코드를 받아오기
    @GetMapping()
    public void requestKakaoLoginScreen(HttpServletResponse response) throws IOException {
        String url = kakaoProperties.getAuthCodeUri() +
                "?client_id=" + kakaoProperties.clientId() +
                "&redirect_uri=" + kakaoProperties.redirectUri() +
                "&response_type=code";

        response.sendRedirect(url);
    }


    // 로그인 후 받은 인가 코드를 통해 access token을 발급 받기
    // 이때, 사용자의 정보와 토큰값을 DB에 저장하기
    @GetMapping("/callback")
    public ResponseEntity<?> getAccessToken(@RequestParam("code") String code) throws IOException {
        // 2. 토큰 발급 받기
        KakaoTokenResponse tokenResponse = kakaoService.getToken(code).getBody();

        if (tokenResponse == null) {
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "토큰 추출 실패", false));
        }

        // 3. 사용자 정보 불러오기
        KakaoUser kakaoUserInfo = kakaoService.getUserInfo(tokenResponse.accessToken());
        if (kakaoUserInfo == null) {
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "사용자 정보 추출 실패", false));
        }

        // 4. DB에 저장하기
        kakaoService.saveToken(tokenResponse, kakaoUserInfo);

        return ResponseEntity.ok(new CommonResponse<>(tokenResponse, "토큰 추출 성공", true));
    }
}
