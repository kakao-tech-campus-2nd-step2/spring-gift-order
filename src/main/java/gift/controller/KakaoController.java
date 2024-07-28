package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.ApiResponse;
import gift.dto.KakaoToken;
import gift.dto.TokenResponseDto;
import gift.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping
    public RedirectView kakaoLogin() {
        return new RedirectView(
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("kauth.kakao.com")
                        .path("/oauth/authorize")
                        .queryParam("client_id", kakaoProperties.clientId())
                        .queryParam("redirect_uri", kakaoProperties.redirectURL())
                        .queryParam("response_type", "code")
                        .build(true).toString()
        );
    }

    @GetMapping("/redirect")
    public ResponseEntity<TokenResponseDto> getTokenAndUserInfo(
            @RequestParam(value = "code") String kakaoCode
    ) {
        KakaoToken kakaoToken = kakaoService.getKakaoToken(kakaoCode);
        TokenResponseDto token = kakaoService.generateToken(kakaoToken.accessToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/unlink")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        kakaoService.unlink(token);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT, "계정 연결해제 성공적"));
    }
}
