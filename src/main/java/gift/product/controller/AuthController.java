package gift.product.controller;

import gift.product.dto.AccessAndRefreshToken;
import gift.product.dto.JwtResponse;
import gift.product.dto.MemberDto;
import gift.product.dto.RegisterSuccessResponse;
import gift.product.property.KakaoProperties;
import gift.product.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseBody
    @PostMapping("/members/register")
    public ResponseEntity<RegisterSuccessResponse> registerMember(
        @RequestBody MemberDto memberDto) {
        authService.register(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new RegisterSuccessResponse("회원가입이 완료되었습니다."));
    }

    @ResponseBody
    @PostMapping("/members/login")
    public ResponseEntity<JwtResponse> loginMember(@RequestBody MemberDto memberDto) {
        JwtResponse jwtResponse = authService.login(memberDto);

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/members/login/kakao")
    public RedirectView loginKakao() {
        return new RedirectView(authService.getKakaoAuthCodeUrl());
    }

    @ResponseBody
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
