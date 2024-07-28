package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.KakaoAuthService;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping
public class KakaoController {

    @Value("${kakao.api-key}")
    private String apiKey;

    private final KakaoAuthService kakaoAuthService;
    private final KakaoMessageService kakaoMessageService;
    private final OrderService orderService;

    public KakaoController(KakaoAuthService kakaoAuthService, KakaoMessageService kakaoMessageService, OrderService orderService) {
        this.kakaoAuthService = kakaoAuthService;
        this.kakaoMessageService = kakaoMessageService;
        this.orderService = orderService;
    }

    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String clientId = kakaoAuthService.getClientId();
        String redirectUri = kakaoAuthService.getRedirectUri();
        String authUrl = "https://kauth.kakao.com/oauth/authorize";
        String url = authUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletRequest request) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        // 액세스 토큰을 세션에 저장
        request.getSession().setAttribute("accessToken", accessToken);
        // 로그인 완료 후 홈 페이지로 리디렉션
        return "redirect:/home";
    }

    @PostMapping("/sendmessage")
    public ResponseEntity<String> sendMessageToMe(@RequestHeader("Authorization") String bearerToken, @RequestBody OrderRequest orderRequest) {
        // Authorization 헤더에서 Bearer 토큰을 추출
        String accessToken = bearerToken.replace("Bearer ", "");
        OrderResponse orderResponse = orderService.createOrder(orderRequest, accessToken);
        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }
}