package gift.order.controller;

import gift.common.config.KakaoProperties;
import gift.common.util.CommonResponse;
import gift.order.dto.KakaoMember;
import gift.order.dto.KakaoTokenResponse;
import gift.order.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/kakao/login")
public class KakaoLoginController {
    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    public KakaoLoginController(KakaoProperties properties, KakaoService kakaoService) {
        this.kakaoProperties = properties;
        this.kakaoService = kakaoService;
    }

    // 카카오 로그인을 통해 인가 코드를 받아오기
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
        KakaoTokenResponse tokenResponse = kakaoService.getToken(code).getBody();
        if (tokenResponse == null) {
            return ResponseEntity.badRequest().body(new CommonResponse<>(null, "토큰 추출 실패", false));
        }
        KakaoMember kakaoMemberInfo = kakaoService.getMemberInfo(tokenResponse.accessToken());


        return ResponseEntity.ok(new CommonResponse<>(tokenResponse, "토큰 추출 성공", true));
    }


}
