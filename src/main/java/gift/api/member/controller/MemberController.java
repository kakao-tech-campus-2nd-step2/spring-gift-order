package gift.api.member.controller;

import gift.api.member.MemberRequest;
import gift.api.member.MemberService;
import gift.global.utils.JwtUtil;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final RestClient restClient;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        restClient = RestClient.create();
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
    public void requestKakaoToken(@RequestParam("code") String code) {
        final String uri = "https://kauth.kakao.com/oauth/token";
        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(uri))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(memberService.createBody(code))
            .retrieve()
            .toEntity(String.class);
    }
}
