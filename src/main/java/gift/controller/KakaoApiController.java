package gift.controller;

import gift.service.KakaoApiService;
import gift.utils.config.KakaoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @PostMapping("/oauth/authorize")
    public String kakaoLogin(@RequestBody String code){
        String kakaoCode = kakaoApiService.createKakaoCode(code);
        return "redirect:"+kakaoCode;
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<?> kakaoToken()
}
