package gift.api.member.controller;

import gift.api.member.MemberService;
import gift.api.member.dto.MemberRequest;
import gift.api.member.dto.TokenResponse;
import gift.api.member.dto.UserInfoResponse;
import gift.global.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid MemberRequest memberRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String accessToken = JwtUtil.generateAccessToken(memberService.register(memberRequest), memberRequest.email(), memberRequest.role());
        responseHeaders.set("Authorization", JwtUtil.generateHeaderValue(accessToken));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberRequest memberRequest, @RequestHeader("Authorization") String token) {
        memberService.login(memberRequest, token.split(" ")[1]);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth/kakao")
    public ResponseEntity<Void> loginKakao(@RequestParam("code") String code) {
        ResponseEntity<TokenResponse> tokenResponse = memberService.obtainToken(code);
        ResponseEntity<UserInfoResponse> userInfoResponse = memberService.obtainUserInfo(tokenResponse);
        memberService.saveKakaoToken(tokenResponse, userInfoResponse);
        memberService.loginKakao(userInfoResponse);
        return ResponseEntity.ok().build();
    }
}
