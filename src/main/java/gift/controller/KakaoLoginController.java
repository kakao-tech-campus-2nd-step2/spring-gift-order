package gift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class KakaoLoginController {
    @Autowired
    private KakaoLoginService kakaoLoginService;

    @GetMapping("")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoAuthService.getAccessToken(code);

        Map<String, String> response = new HashMap<>();
        response.put("authorizationCode", code);
        response.put("accessToken", accessToken);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
