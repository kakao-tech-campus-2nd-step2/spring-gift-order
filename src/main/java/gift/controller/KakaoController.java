package gift.controller;

import gift.service.KakaoLoginService;
import gift.service.kakao.TokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Kakao Login API", description = "카카오톡 로그인 API")
@RequestMapping("/kakao")
@RestController
public class KakaoController {

    private final KakaoLoginService loginService;

    public KakaoController(KakaoLoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "카카오 로그인 페이지로 리다이렉트", description = "카카오 로그인 페이지로 사용자를 리다이렉트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "카카오 로그인 페이지로 리다이렉트"),
    })
    @GetMapping("/login")
    public ResponseEntity<Void> redirectLoginForm() {
        HttpHeaders redirectHeaders = loginService.getRedirectHeaders();

        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(redirectHeaders)
                .build();
    }

    @Hidden
    @GetMapping("/oauth")
    public ResponseEntity<TokenResponse> login(@RequestParam String code) {
        TokenResponse tokenResponse = loginService.processKakaoAuth(code);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }

}
