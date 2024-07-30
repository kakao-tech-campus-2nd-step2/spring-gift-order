package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.SendMessageRequest;
import gift.service.KakaoAuthService;
import gift.service.KakaoMessageService;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping
public class KakaoController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

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
    public String callback(@RequestParam("code") String code, HttpServletRequest request) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        request.getSession().setAttribute("accessToken", accessToken);
        return "redirect:/home";
    }

    /** 프론트엔드와 회의 후 카카오 로그인 사용하지 말자고 정하여 필요시 활성화
    @PostMapping("/sendmessage")
    public ResponseEntity<String> sendMessageToMe(@RequestHeader("Authorization") String bearerToken, @RequestBody OrderRequest orderRequest) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(bearerToken, orderRequest);
        orderService.processOrderAndSendMessage(sendMessageRequest);
        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    } **/
}