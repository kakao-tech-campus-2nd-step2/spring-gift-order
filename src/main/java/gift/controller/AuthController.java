package gift.controller;

import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.service.AuthService;
import gift.service.KaKaoLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Account", description = "회원가입, 로그인 등 사용자 계정과 관련된 API Controller")
public class AuthController {

    private final AuthService authService;
    private final KaKaoLoginService kaKaoLoginService;

    public AuthController(AuthService authService, KaKaoLoginService kaKaoLoginService) {
        this.authService = authService;
        this.kaKaoLoginService = kaKaoLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> memberRegister(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.addMember(authRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> memberLogin(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @GetMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestParam("code") String code) {
        return new ResponseEntity<>(kaKaoLoginService.kakaoLogin(code), HttpStatus.OK);
    }
}
