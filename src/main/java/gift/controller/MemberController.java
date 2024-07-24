package gift.controller;

import gift.dto.*;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberRegisterResponseDto> signUp(@RequestBody MemberRegisterRequestDto request) {
        return ResponseEntity.ok(memberService.signUpMember(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberRequestDto request) {
        String token = memberService.loginMember(request);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
