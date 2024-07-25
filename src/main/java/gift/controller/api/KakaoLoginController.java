package gift.controller.api;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.TokenResponse;
import gift.repository.KakaoAccessTokenRepository;
import gift.service.JwtTokenService;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final KakaoAccessTokenRepository accessTokenRepository;

    public KakaoLoginController(KakaoLoginService kakaoLoginService, MemberService memberService, JwtTokenService jwtTokenService, KakaoAccessTokenRepository accessTokenRepository) {
        this.kakaoLoginService = kakaoLoginService;
        this.memberService = memberService;
        this.jwtTokenService = jwtTokenService;
        this.accessTokenRepository = accessTokenRepository;
    }

    @GetMapping("/")
    public ResponseEntity<TokenResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoLoginService.getToken(code);

        String email = kakaoLoginService.getEmail(kakaoToken.accessToken());

        Long memberId = memberService.findMemberIdByEmail(email);
        TokenResponse tokenResponse = jwtTokenService.generateToken(memberId);
        //이거 서비스층으로 변경
        accessTokenRepository.saveAccessToken(memberId, kakaoToken.accessToken());

        return ResponseEntity.ok().body(tokenResponse);
    }
}
