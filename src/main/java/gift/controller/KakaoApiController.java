package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.KakaoTextMessageRequestDto;
import gift.service.ExternalAPIService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="kakao-api",description = "카카오 api 연동 API")
@RequestMapping("/kakao")
@RestController
public class KakaoApiController {
    public final ExternalAPIService externalAPIService;

    public KakaoApiController(ExternalAPIService externalAPIService) {
        this.externalAPIService = externalAPIService;
    }

    @PostMapping("/message")
    public ResponseEntity<Integer> sendKakaoMessageToMe(@RequestBody KakaoTextMessageRequestDto kakaoTextMessageRequestDto) throws JsonProcessingException {
        return externalAPIService.sendKakaoMessageToMe(kakaoTextMessageRequestDto);
    }
}
