package gift.auth.api;

import gift.auth.application.KakaoAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping
    public String redirectKakaoLogin() {
        return "redirect:" + kakaoAuthService.getKakaoAuthUrl();
    }

    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<Object> handleKakaoCallback(@RequestParam("code") String code) throws Exception {
        ResponseEntity<Object> response = kakaoAuthService.getResponseOfKakaoLogin(code);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        }
        return ResponseEntity.internalServerError()
                .body("로그인에 실패하였습니다.");
    }

}
