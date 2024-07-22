package gift.auth.controller;

import gift.auth.dto.LoginReqDto;
import gift.auth.service.AuthService;
import gift.auth.token.AuthToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestHeader("Authorization") String header, @RequestBody LoginReqDto loginReqDto) {
        AuthToken token = authService.login(header, loginReqDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestParam("code") String code, HttpServletResponse response) {
        AuthToken token = authService.login(code);
        return ResponseEntity.ok(token);
    }
}
