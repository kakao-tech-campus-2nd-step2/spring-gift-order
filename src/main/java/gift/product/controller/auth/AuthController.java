package gift.product.controller.auth;

import gift.product.dto.auth.AccessAndRefreshToken;
import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.RegisterSuccessResponse;
import gift.product.service.AuthService;
import java.net.URI;
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

    @PostMapping("/members/register")
    public ResponseEntity<RegisterSuccessResponse> registerMember(
        @RequestBody MemberDto memberDto) {
        authService.register(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new RegisterSuccessResponse("회원가입이 완료되었습니다."));
    }

    @PostMapping("/members/login")
    public ResponseEntity<JwtResponse> loginMember(@RequestBody MemberDto memberDto) {
        JwtResponse jwtResponse = authService.login(memberDto);

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/members/login/kakao")
    public ResponseEntity<Void> loginKakao() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authService.getKakaoAuthCodeUrl()));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping
    public ResponseEntity<AccessAndRefreshToken> getKakaoJwt(@RequestParam(name = "code") String code) {
        AccessAndRefreshToken accessAndRefreshToken = authService.getAccessAndRefreshToken(code);
        return ResponseEntity.ok(accessAndRefreshToken);
    }

    @PostMapping("/members/login/kakao/unlink")
    public ResponseEntity<Long> unlinkKakaoAccount(@RequestParam(name = "accessToken") String accessToken) {
        return ResponseEntity.ok(authService.unlinkKakaoAccount(accessToken));
    }
}
