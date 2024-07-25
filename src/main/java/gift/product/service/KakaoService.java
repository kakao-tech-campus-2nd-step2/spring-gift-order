package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.DID_NOT_GET_RESPONSE;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.exception.InvalidIdException;
import gift.product.exception.ResponseException;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties properties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public KakaoService(
        KakaoProperties properties,
        MemberRepository memberRepository,
        JwtUtil jwtUtil
    ) {
        this.properties = properties;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String authCode) {
        String accessToken = getAccessToken(authCode);
        String kakaoMemberId = parsingAccessToken(accessToken);
        Member member = signUpAndLogin(kakaoMemberId, accessToken);
        return jwtUtil.generateToken(member.getEmail());
    }

    public String getAuthCode() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
            + "&redirect_uri=" + properties.redirectUrl()
            + "&client_id=" + properties.clientId();
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(code);
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new ResponseException(DID_NOT_GET_RESPONSE);
        return response.get("access_token").toString();
    }

    public String parsingAccessToken(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var response = client.get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new ResponseException(DID_NOT_GET_RESPONSE);
        return response.get("id").toString();
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }

    public Member signUpAndLogin(String kakaoMemberId, String accessToken) {
        if(!memberRepository.existsByEmail(kakaoMemberId))
            return memberRepository.save(new Member(kakaoMemberId, accessToken));
        return memberRepository.findByEmail(kakaoMemberId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }
}