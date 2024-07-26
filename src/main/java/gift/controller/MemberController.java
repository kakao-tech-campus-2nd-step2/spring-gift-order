package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.MemberDTO;
import gift.dto.MemberPasswordDTO;
import gift.exception.NoSuchMemberException;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute MemberDTO memberDTO) {
        memberService.register(memberDTO);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public Object login(@Valid @RequestBody MemberDTO memberDTO) {
        try {
            return ResponseEntity.ok().body(memberService.login(memberDTO));
        } catch (
            NoSuchMemberException e) {
            return "register";
        }
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(
        @LoginMember MemberDTO memberDTO,
        @Valid @RequestBody MemberPasswordDTO memberPasswordDTO
    ) {
        return ResponseEntity.ok().body(memberService.changePassword(memberDTO, memberPasswordDTO));
    }
}
