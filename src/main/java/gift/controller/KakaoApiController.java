package gift.controller;

import gift.dto.KakaoMessageResponseDto;
import gift.service.ExternalAPIService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/kakao")
@Controller
public class KakaoApiController {
    public final ExternalAPIService externalAPIService;

    public KakaoApiController(ExternalAPIService externalAPIService) {
        this.externalAPIService = externalAPIService;
    }

    @PostMapping("/message")
    public ResponseEntity<KakaoMessageResponseDto> sendKakaoMessageToMe(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody KakaoMessageRequestDto kakaoMessageRequestDto) {
        String token = authorizationHeader.replace("Basic ", "");
        return externalAPIService.sendKakaoMessageToMe(token, kakaoMessageRequestDto);
    }
}
