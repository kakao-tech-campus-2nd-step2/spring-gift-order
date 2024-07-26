package gift.authentication.restapi;

import gift.authentication.restapi.dto.response.LoginResponse;
import gift.core.domain.authentication.OAuthService;
import gift.core.domain.authentication.OAuthType;
import gift.core.domain.authentication.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "OAuth")
public class KakaoOAuthController {
    private final OAuthService oAuthService;

    public KakaoOAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/api/oauth/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인을 수행합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "카카오 액세스 토큰을 반환합니다",
            content = @Content(mediaType = "application/json"),
            useReturnTypeSchema = true
    )
    public LoginResponse login(@RequestParam("code") String code) {
        Token token = oAuthService.authenticate(OAuthType.KAKAO, code);

        return LoginResponse.of(token);
    }
}
