package gift.controller.api;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.TokenResponse;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;
    private final TokenService tokenService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, MemberService memberService, TokenService tokenService) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public ResponseEntity<TokenResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoLoginService.getToken(code);

        String email = kakaoLoginService.getEmail(kakaoToken.access_token());

        System.out.println(email);
        Long memberId = memberService.findMemberIdByEmail(email);
        TokenResponse tokenResponse = tokenService.generateToken(memberId);
        System.out.println(tokenResponse);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
