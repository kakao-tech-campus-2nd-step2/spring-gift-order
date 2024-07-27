package gift.controller;


import static gift.util.ResponseEntityUtil.responseError;

import gift.dto.betweenClient.JwtDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.exception.BadRequestExceptions.EmailAlreadyHereException;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PropertySource("classpath:application-secret.properties")
@PropertySource("classpath:application-kakao-login.properties")
@Validated
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${kakao-rest-api-key}")
    private String clientId;

    @Value("${kakao-redirect-uri}")
    private String redirectUri;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid MemberDTO memberDTO) {
        String token;
        try {
            memberDTO.setAccountType("basic");
            memberService.register(memberDTO);
            token = jwtUtil.generateToken(memberDTO);
        } catch (RuntimeException e) {
            if(e instanceof EmailAlreadyHereException)
                return responseError(e, HttpStatus.CONFLICT);
            return responseError(e);
        }
        return new ResponseEntity<>(new JwtDTO(token), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public String login(Model model){
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberDTO memberDTO) {
        String token;
        try {
            memberDTO.setAccountType("basic");
            memberService.login(memberDTO);
            token = jwtUtil.generateToken(memberDTO);
        } catch (RuntimeException e) {
            return responseError(e, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new JwtDTO(token), HttpStatus.OK);
    }
}
