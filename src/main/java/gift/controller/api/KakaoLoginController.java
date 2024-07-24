package gift.controller.api;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.TokenResponse;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import gift.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, MemberService memberService, JwtTokenService jwtTokenService) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/")
    public ResponseEntity<TokenResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoLoginService.getToken(code);

        String email = kakaoLoginService.getEmail(kakaoToken.access_token());

        Long memberId = memberService.findMemberIdByEmail(email);
        TokenResponse tokenResponse = jwtTokenService.generateToken(memberId);

        return ResponseEntity.ok().body(tokenResponse);
    }
}
