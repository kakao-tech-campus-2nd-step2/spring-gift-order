package gift.controller.auth;

import gift.exception.UnauthorizedException;
import gift.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static void validateUserOrAdmin(LoginResponse member, UUID id) {
        if (!member.id().equals(id) && !member.isAdmin()) {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginRequest member) {
        Token token = authService.login(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.token());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(token);
    }

    @GetMapping("/authorize")
    public void getAuthorizationCode(HttpServletResponse response) throws IOException {
        response.sendRedirect(authService.getAuthorizationUrl());
    }

    @GetMapping("/kakaoLogin")
    public ResponseEntity<KakaoToken> loginWithKakao(@RequestParam String code) {
        KakaoToken token = authService.getKakaoToken(code);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.accessToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers)
            .body(token);
    }
}