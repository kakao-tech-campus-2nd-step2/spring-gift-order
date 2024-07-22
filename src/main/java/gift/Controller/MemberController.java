package gift.Controller;

import gift.DTO.RequestMemberDTO;
import gift.DTO.ResponseLoginDTO;
import gift.Service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody RequestMemberDTO member){
        memberService.signUpUser(member);
        return "signUp successed";
    }


    @PostMapping("/login")
    public ResponseLoginDTO loginUser(@RequestBody RequestMemberDTO member) {
        return new ResponseLoginDTO(memberService.loginUser(member));
    }

}
