package gift.controller;

import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static gift.domain.LoginType.NORMAL;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @Autowired
    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
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
        Map<String, String> response = memberService.handleKakaoLogin(code);
        return ResponseEntity.ok(response);

    }
}
