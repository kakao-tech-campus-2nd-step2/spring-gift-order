package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProperties;
import gift.dto.LoginResponse;
import gift.dto.OAuthLoginRequest;
import gift.exception.auth.UnauthorizedException;
import gift.exception.user.MemberNotFoundException;
import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class OAuthService {
    private final MemberRepository memberRepository;
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OAuthService(MemberRepository memberRepository, KakaoProperties kakaoProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>() {
        };
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        try {
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String scope = responseBody.get("scope") != null ? responseBody.get("scope").asText() : "";
            if (!scope.contains("talk_message")) {
                throw new UnauthorizedException("[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.");
            }
            return responseBody.get("access_token") != null ? responseBody.get("access_token").asText() : "";
        } catch (JsonProcessingException e) {
            throw new InternalException("서버 내부 오류: " + e.getMessage());
        }
    }

    public String getMemberProfileId(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Object> kakaoProfileRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, kakaoProfileRequest, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode id = jsonNode.get("id");
            return id != null ? id.asText() : null;
        } catch (Exception e) {
            throw new InternalException("서버 내부 오류: " + e.getMessage());
        }
    }

    public Member register(OAuthLoginRequest request) {
        Member member = new Member(
                request.id(),
                "password"
        );
        return memberRepository.save(member);
    }

    public LoginResponse login(OAuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.id())
                .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
        LoginResponse response = new LoginResponse(JwtUtil.createToken(member.getEmail()));
        return response;
    }
    public boolean isMemberAlreadyRegistered(OAuthLoginRequest request) {
        return memberRepository.findByEmail(request.id()).isPresent();
    }
}
