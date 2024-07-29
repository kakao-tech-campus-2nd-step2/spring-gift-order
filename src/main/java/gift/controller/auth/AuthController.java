package gift.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.member.MemberResponse;
import gift.exception.AuthorizationCodeException;
import gift.exception.UnauthorizedException;
import gift.service.AuthService;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final KakaoTokenService kakaoTokenService;

    public AuthController(AuthService authService, MemberService memberService,
        KakaoTokenService kakaoTokenService) {
        this.authService = authService;
        this.memberService = memberService;
        this.kakaoTokenService = kakaoTokenService;
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
    public void getAuthorizationCode(HttpServletResponse response) {
        try {
            response.sendRedirect(authService.getAuthorizationUrl());
        } catch (Exception e) {
            throw new AuthorizationCodeException();
        }
    }

    @GetMapping("/kakaoLogin")
    public ResponseEntity<Token> loginWithKakao(@RequestParam String code) {
        KakaoTokenResponse kakaoToken = authService.getKakaoToken(code);
        KakaoMemberInfoResponse memberInfo = authService.getMemberInfo(kakaoToken);
        MemberResponse member = memberService.findByEmail(memberInfo.email());
        kakaoTokenService.save(member.id(), kakaoToken);
        Token token = new Token(JwtUtil.generateToken(member.id(), member.email()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.token());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(token);
    }
}