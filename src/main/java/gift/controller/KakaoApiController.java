package gift.controller;

import gift.service.KakaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    /**
     * 인가 코드를 String으로 받고 이 코드로 카카오서버에 인가토큰(access_token)요청
     * @param code 인가 코드
     */
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam(value = "code") String code) {
        String accessToken = kakaoApiService.getAccessToken(code);
        kakaoApiService.kakaoLogin(accessToken);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

}
