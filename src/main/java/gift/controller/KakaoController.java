package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.KakaoProperties;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.value.KakaoString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Controller
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final OrderService orderService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService, OrderService orderService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
        this.orderService = orderService;
    }

    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String clientId = new KakaoString(kakaoProperties.getClientId()).removeNewlines();
        String redirectUri = new KakaoString(kakaoProperties.getRedirectUri()).removeNewlines();
        String authUrl = new KakaoString(kakaoProperties.getAuthUrl()).removeNewlines();
        String url = authUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(url);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletRequest request) {
        String accessToken = kakaoService.getAccessToken(code);
        // 액세스 토큰을 세션에 저장
        request.getSession().setAttribute("accessToken", accessToken);
        // 로그인 완료 후 홈 페이지로 리디렉션
        return "redirect:/home";
    }

    @GetMapping("/myinfo")
    public ResponseEntity<String> getMyInfo(@RequestHeader("Authorization") String accessToken) {
        String apiUrl = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return response;
    }

    @PostMapping("/sendmessage")
    public ResponseEntity<String> sendMessageToMe(@RequestHeader("Authorization") String accessToken, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        kakaoService.sendKakaoMessage(orderResponse);
        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }
}