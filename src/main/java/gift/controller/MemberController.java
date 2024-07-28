package gift.controller;

import gift.domain.Member;
import gift.dto.TokenResponse;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Member member) {
        try {
            memberService.register(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Member member) {
        try {
            String token = memberService.login(member);
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
