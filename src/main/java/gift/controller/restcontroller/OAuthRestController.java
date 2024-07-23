package gift.controller.restcontroller;

import gift.controller.dto.response.TokenResponse;
import gift.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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

    public OAuthRestController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/kakao/token")
    @Operation(summary = "카카오 로그인", description = "카카오로 로그인합니다.")
    public ResponseEntity<TokenResponse> kakaoToken(@RequestParam("code") @NotNull String code) {
        TokenResponse response = oAuthService.signIn(code);
        return ResponseEntity.ok().body(response);
    }
}
