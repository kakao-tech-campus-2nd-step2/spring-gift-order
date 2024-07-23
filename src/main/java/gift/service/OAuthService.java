package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.enums.Role;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.TokenProvider;
import gift.service.dto.KakaoInfoDto;
import gift.service.dto.KakaoTokenDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final RestClient client;
    private final KakaoProperties properties;

    public OAuthService(MemberRepository memberRepository, TokenProvider tokenProvider, ObjectMapper objectMapper, KakaoProperties kakaoProperties) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.client = RestClient.builder().build();
        this.properties = kakaoProperties;
    }

    public TokenResponse signIn(String code) {
        return signIn(code, properties.redirectUrl());
    }

    public TokenResponse signIn(String code, String redirectUrl) {
        String accessToken = getKakaoAccessToken(code, redirectUrl);
        String email = getKakaoMemberInfo(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", Role.USER)));
        String token = tokenProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return TokenResponse.from(token);
    }

    public String getKakaoAccessToken(String code, String redirectUrl) {
        return client.post()
                .uri(URI.create(properties.tokenUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForAccessToken(code, redirectUrl))
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoTokenDto.class)
                                .access_token();
                    }
                    throw new AuthenticationException("Kakao login failed");
                });
    }

    public String getKakaoMemberInfo(String accessToken) {
        return client.post()
                .uri(URI.create(properties.memberInfoUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoInfoDto.class).id() + "@Kakao";
                    }
                    throw new AuthenticationException("Kakao Info failed");
                });
    }

    private @NotNull LinkedMultiValueMap<String, String> createBodyForAccessToken(String code, String redirectUrl) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);
        return body;
    }
}
