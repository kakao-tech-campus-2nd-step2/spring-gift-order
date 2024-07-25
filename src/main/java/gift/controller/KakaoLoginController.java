package gift.controller;

import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.exception.DuplicateMemberEmailException;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("members/kakao/login")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse tokenResponse = kakaoLoginService.getKakaoToken(code);
        KakaoProfileResponse profileResponse = kakaoLoginService.getUserProfile(tokenResponse.accessToken());

        String email = profileResponse.kakaoAccount().email();
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "이메일을 가져올 수 없습니다."), HttpStatus.BAD_REQUEST);
        }

        Member member;
        try {
            member = memberService.register(new MemberRequest(email, null));
        } catch (DuplicateMemberEmailException e) {
            member = memberService.findByEmail(email);
        }

        String accessToken = tokenService.saveToken(member, tokenResponse.accessToken());

        Map<String, String> response = new HashMap<>();
        response.put("authorizationCode", code);
        response.put("accessToken", accessToken);
        response.put("email", email);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
