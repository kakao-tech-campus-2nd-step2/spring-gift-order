package gift.controller;

import gift.domain.KakaoLoginResponse;
import gift.domain.MemberRequest;
import gift.domain.MenuRequest;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Value("${my.client_id}")
    private String client_id;

    @Value("${my.code}")
    private String code;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(
            @RequestBody MemberRequest memberRequest
    ) {
        memberService.join(memberRequest);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody MemberRequest memberRequest
    ) {
        String jwt = memberService.login(memberRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    @PostMapping("/loginByKakao")
    public ResponseEntity<String> loginByKakao(){
        var url = "https://kauth.kakao.com/oauth/token";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", code); // authorizationCode 값을 여기 넣으세요

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(request, KakaoLoginResponse.class);

        System.out.println("Response: " + response.getBody());
        System.out.println(response.getBody().access_token());
        headers = new HttpHeaders();
        headers.add("Authorization",response.getBody().access_token() );
        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("changePassword is not allowed");
    }

    @PostMapping("/findPassword")
    public ResponseEntity findPassword(
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("findPassword is not allowed");
    }
}
