package gift.controller;

import gift.domain.LoginType;
import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.exception.DuplicateMemberEmailException;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static gift.domain.LoginType.KAKAO;
import static gift.domain.LoginType.NORMAL;

@RestController
@RequestMapping("/members")
public class MemberController {

    private static final String KAKAO_EMAIL_SUFFIX = "@kakao.com";

    private final MemberService memberService;
    private final TokenService tokenService;
    private final KakaoLoginService kakaoLoginService;

    @Autowired
    public MemberController(MemberService memberService, TokenService tokenService, KakaoLoginService kakaoLoginService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.register(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.authenticate(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam String code) {
        KakaoTokenResponse tokenResponse = kakaoLoginService.getKakaoToken(code);

        KakaoProfileResponse profileResponse = kakaoLoginService.getUserProfile(tokenResponse.accessToken());

        String email = profileResponse.kakaoAccount().profile().nickname() + KAKAO_EMAIL_SUFFIX;

        if (KAKAO_EMAIL_SUFFIX.equals(email)) {
            return new ResponseEntity<>(Map.of("error", "이메일을 가져올 수 없습니다."), HttpStatus.BAD_REQUEST);
        }

        LoginType loginType = KAKAO;
        Member member;
        try {
            member = memberService.register(new MemberRequest(email, "kakao"), loginType);
        } catch (DuplicateMemberEmailException e) {
            member = memberService.findByEmailAndLoginType(email, loginType);
        }

        String accessToken = tokenService.saveToken(member, tokenResponse.accessToken());

        Map<String, String> response = new HashMap<>();
        response.put("authorizationCode", code);
        response.put("accessToken", accessToken);
        response.put("email", email);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
