package gift.controller;

import gift.service.KakaoProperties;
import gift.service.KakaoService;
import gift.value.KakaoString;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
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
    public ResponseEntity<String> sendMessageToMe(@RequestHeader("Authorization") String accessToken) {
        String apiUrl = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", "This is a test message.");
        templateObject.put("link", Map.of("web_url", "http://example.com"));
        templateObject.put("button_title", "Open Web");

        Map<String, Object> body = new HashMap<>();
        body.put("template_object", templateObject);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        return response;
    }
}