package gift.controller.restcontroller;

import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth", description = "OAuth API")
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthRestController {

    private final OAuthService oAuthService;
    private final KakaoProperties properties;

    public OAuthRestController(OAuthService oAuthService, KakaoProperties properties) {
        this.oAuthService = oAuthService;
        this.properties = properties;
    }

    @GetMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 주소를 반환합니다.")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", properties.loginUrl() + properties.clientId())
                .build();
    }

    @GetMapping("/kakao/login/callback")
    @Operation(summary = "카카오 로그인 리다이렉션", description = "카카오 액세스 토큰을 발급받습니다.")
    public ResponseEntity<TokenResponse> kakaoToken(@RequestParam("code") @NotNull String code) {
        TokenResponse response = oAuthService.signIn(code);
        return ResponseEntity.ok().body(response);
    }
}
