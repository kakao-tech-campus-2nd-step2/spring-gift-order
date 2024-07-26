package gift.controller;


import gift.dto.KakaoMember;
import gift.dto.KakaoTokenResponseDto;
import gift.service.KakaoService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao/login")
public class KakaoController {
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoController(KakaoService kakaoService, MemberService memberService) {
        this.jwtUtil = new JwtUtil();
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8b0993ea8425d3f401667223d8d6b1a7&redirect_uri=http://localhost:8080/kakao/login/token");

    }

    @GetMapping("/token")
    public ResponseEntity<?> token(@RequestParam("code") String code) {
        Map<String, Object> responseBody = new HashMap<>();
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code);
        KakaoMember kakaoMember = kakaoService.getKakaoProfile(kakaoTokenResponseDto);
        String token = memberService.loginKakaoMember(kakaoMember);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(responseBody);
    }

}
