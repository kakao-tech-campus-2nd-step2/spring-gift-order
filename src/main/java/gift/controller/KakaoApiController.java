package gift.controller;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoOrderResponse;
import gift.service.KakaoApiService;
import gift.utils.config.KakaoProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @PostMapping("/oauth/orders")
    public ResponseEntity<KakaoApiDTO.KakaoOrderResponse> kakaoOrder(@RequestHeader("Authorization") String authHeader,
        @RequestBody KakaoApiDTO.KakaoOrderRequest kakaoOrderRequest){
        String accessToken = authHeader.substring(7);
        KakaoOrderResponse kakaoOrderResponse = kakaoApiService.kakaoOrder(kakaoOrderRequest,accessToken);
        return ResponseEntity.ok(kakaoOrderResponse);
    }
}
