package gift.auth.service;

import gift.auth.domain.AuthInfo;
import gift.auth.domain.KakaoProperties;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.member.domain.KakaoMember;
import gift.auth.dto.LoginRequestDto;
import gift.auth.exception.KakaoTokenException;
import gift.global.security.TokenManager;
import gift.member.domain.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import gift.member.service.KakaoMemberService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;
    private final RestTemplate restTemplate;
    private final KakaoMemberService kakaoMemberService;

    public static final String BEARER_TYPE = "Bearer";
    private final KakaoProperties kakaoProperties;
    private static final String AUTHORIZE_URL_FORMAT = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    public AuthService(MemberRepository memberRepository, TokenManager tokenManager, RestTemplate restTemplate, KakaoMemberService kakaoMemberService, KakaoProperties kakaoProperties) {
        this.memberRepository = memberRepository;
        this.tokenManager = tokenManager;
        this.restTemplate = restTemplate;
        this.kakaoMemberService = kakaoMemberService;
        this.kakaoProperties = kakaoProperties;
    }

    public Map<String, String> login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password())
                .orElseThrow(MemberNotFoundException::new);
        String accessToken = BEARER_TYPE + " " + tokenManager.createAccessToken(new AuthInfo(member));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", accessToken);
        return headers;
    }

    public String getKakaoAuthUrl() {
        return String.format(AUTHORIZE_URL_FORMAT, kakaoProperties.getClientId(), kakaoProperties.getRedirectUri());
    }

    public Map<String, String> kakaoLogin(String code) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(TOKEN_URL));

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(request, KakaoTokenResponseDto.class);

        if (response.getStatusCode() != HttpStatus.OK || Objects.isNull(response.getBody())) {
            throw new KakaoTokenException();
        }

        KakaoMember kakaoMember = kakaoMemberService.createMember(response.getBody().toKakaoMember());
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("KakaoAuthorization", kakaoMember.getAccessToken());

        return responseHeaders;
    }
}
