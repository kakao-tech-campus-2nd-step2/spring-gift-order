package gift.controller.api;

import gift.dto.response.JwtTokenResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.service.KakaoApiService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;

    public KakaoLoginController(MemberService memberService, TokenService tokenService, KakaoApiService kakaoApiService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/")
    public ResponseEntity<JwtTokenResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoApiService.getKakaoToken(code);

        String email = kakaoApiService.getMemberEmail(kakaoToken.accessToken());
        Long memberId = memberService.findMemberIdByEmail(email);

        JwtTokenResponse JwtTokenResponse = tokenService.generateJwtToken(memberId);
        tokenService.saveKakaoAccessToken(memberId, kakaoToken);

        return ResponseEntity.ok().body(JwtTokenResponse);
    }
}
