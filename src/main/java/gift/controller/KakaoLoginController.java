package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.KakaoInfo;
import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao")
    public ResponseEntity<KakaoInfo> kakaoLogin(@RequestParam("code") String code) {
        // 1. 인가 코드 받기 (code)

        try {
            // 2. 인가 코드로 토큰 발급
            String accessToken = kakaoService.getKakaoToken(code);
            // 3. 토큰에서 사용자 정보 가져오기
            KakaoInfo kakaoInfo = kakaoService.getKakaoInfo(accessToken);

            return ResponseEntity.ok(kakaoInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
