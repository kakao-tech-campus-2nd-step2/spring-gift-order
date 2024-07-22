package gift.controller;

import gift.service.KakaoApiService;
import gift.utils.config.KakaoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/oauth/authorize")
    public String kakaoLogin(){
        String kakaoCode = kakaoApiService.createKakaoCode();
        return "redirect:"+kakaoCode;
    }

    @GetMapping("/oauth/token")
    public ResponseEntity<String> kakaoToken(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String error_description,
        @RequestParam(required = false) String state){

        String kakaoToken = kakaoApiService.createKakaoToken(code, error, error_description, state);

        return ResponseEntity.ok(kakaoToken);
    }
}
