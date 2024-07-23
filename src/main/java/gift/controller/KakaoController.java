package gift.controller;

import gift.config.KakaoProperties;
import gift.domain.Member;
import gift.dto.TokenResponse;
import gift.service.KakaoLoginService;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class KakaoController {

    private MemberService memberService;
    private KakaoLoginService kakaoLoginService;
    private KakaoProperties kakaoProperties;

    public KakaoController(MemberService memberService, KakaoLoginService kakaoLoginService, KakaoProperties kakaoProperties) {
        this.memberService = memberService;
        this.kakaoLoginService = kakaoLoginService;
        this.kakaoProperties = kakaoProperties;
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<Object> kakaoLogin(@RequestParam String code) {
        try {
            String accessToken = kakaoLoginService.getAccessToken(code);
            Map<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            String email = null;
            if (kakaoAccount != null) {
                email = "kakao_" + userInfo.get("id") + "@kakao.com";
            }

            Member member = memberService.findByEmail(email);

            if (member == null) {
                String password = memberService.generateTemporaryPassword();
                member = new Member(null, email, password);
                memberService.register(member);
            }

            String token = memberService.generateToken(member);

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오 로그인 실패: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> handleKakaoCallback(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();
        String postUrl = "http://localhost:8080/kakao/login?code=" + code;
        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("로그인 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패");
        }
    }

}
