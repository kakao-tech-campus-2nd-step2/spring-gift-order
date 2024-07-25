package gift.product.controller.auth;

import gift.product.dto.auth.OAuthJwt;
import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.RegisterSuccessResponse;
import gift.product.model.Member;
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
    private final String KAKAO_AUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_UNLINK_USER_URL = "https://kapi.kakao.com/v1/user/unlink";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

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
    public ResponseEntity<JwtResponse> getKakaoJwt(@RequestParam(name = "code") String code) {
        OAuthJwt OAuthJwt = authService.getOAuthToken(code, KAKAO_AUTH_TOKEN_URL);
        Member member = authService.registerKakaoMember(OAuthJwt.accessToken(), KAKAO_USER_INFO_URL);
        JwtResponse jwtResponse = authService.getToken(member, OAuthJwt);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/members/login/kakao/unlink")
    public ResponseEntity<Long> unlinkKakaoAccount(@RequestParam(name = "accessToken") String oAuthAccessToken) {
        return ResponseEntity.ok(authService.unlinkKakaoAccount(oAuthAccessToken, KAKAO_UNLINK_USER_URL));
    }
}
