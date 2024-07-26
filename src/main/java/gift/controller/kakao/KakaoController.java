package gift.controller.kakao;

import gift.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String authCode) {
        String accessToken = kakaoService.sendTokenRequest(authCode);
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }

}
