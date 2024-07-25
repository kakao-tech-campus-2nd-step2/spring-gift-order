package gift.api.member.controller;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

import gift.api.member.config.KakaoProperties;
import gift.api.member.dto.MemberRequest;
import gift.api.member.MemberService;
import gift.api.member.dto.TokenResponse;
import gift.global.utils.JwtUtil;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final KakaoProperties properties;
    private final RestClient restClient;

    public MemberController(MemberService memberService, KakaoProperties properties) {
        this.memberService = memberService;
        this.properties = properties;
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
        ResponseEntity<TokenResponse> tokenResponse = restClient.post()
            .uri(URI.create(properties.url().token()))
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(memberService.createBody(code))
            .retrieve()
            .toEntity(TokenResponse.class);

        ResponseEntity<String> userResponse = restClient.get()
            .uri(properties.url().user(), uriBuilder -> uriBuilder
                .queryParam("property_keys", "[\"kakao_account.email\"]")
                .build())
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .header(AUTHORIZATION, "Bearer " + tokenResponse.getBody().accessToken())
            .retrieve()
            .toEntity(String.class);
    }
}
