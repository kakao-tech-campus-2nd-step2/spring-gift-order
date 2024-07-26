package gift.controller.api;

import gift.client.KakaoApiClient;
import gift.client.requestBody.KakaoTokenRequestBody;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.JwtTokenResponse;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;
    private final TokenService tokenService;

    public KakaoLoginController(KakaoApiClient kakaoApiClient, MemberService memberService, TokenService tokenService) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @GetMapping("/")
    public ResponseEntity<JwtTokenResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoApiClient.getKakaoToken(new KakaoTokenRequestBody(code));

        String email = kakaoApiClient.getMemberEmail(kakaoToken.accessToken());
        Long memberId = memberService.findMemberIdByEmail(email);

        JwtTokenResponse JwtTokenResponse = tokenService.generateJwtToken(memberId);
        tokenService.saveKakaoAccessToken(memberId, kakaoToken);

        return ResponseEntity.ok().body(JwtTokenResponse);
    }
}
