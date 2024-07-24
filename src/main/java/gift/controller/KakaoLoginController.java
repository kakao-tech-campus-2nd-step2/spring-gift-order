package gift.controller;

import gift.service.KakaoLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoLoginService.getAccessToken(code);

        Map<String, String> response = new HashMap<>();
        response.put("authorizationCode", code);
        response.put("accessToken", accessToken);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
