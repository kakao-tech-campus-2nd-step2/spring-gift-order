package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.component.KakaoApiComponent;
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
    private final KakaoApiComponent kakaoApiComponent;

    public OAuthService(MemberRepository memberRepository, KakaoApiComponent kakaoApiComponent) {
        this.memberRepository = memberRepository;
        this.kakaoApiComponent = kakaoApiComponent;
    }

    public String getAccessToken(String code) {
        return kakaoApiComponent.getAccessToken(code);
    }

    public String getMemberProfileId(String accessToken) {
        return kakaoApiComponent.getMemberProfileId(accessToken);
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
