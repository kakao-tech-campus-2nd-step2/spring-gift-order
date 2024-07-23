package gift.service;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.TokenProvider;
import gift.util.KakaoApiUtil;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final KakaoProperties properties;
    private final KakaoApiUtil kakaoApiUtil;

    public OAuthService(MemberRepository memberRepository, TokenProvider tokenProvider, KakaoProperties kakaoProperties, KakaoApiUtil kakaoApiUtil) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.properties = kakaoProperties;
        this.kakaoApiUtil = kakaoApiUtil;
    }

    public TokenResponse signIn(String code) {
        return signIn(code, properties.redirectUrl());
    }

    public TokenResponse signIn(String code, String redirectUrl) {
        String accessToken = kakaoApiUtil.getKakaoAccessToken(code, redirectUrl);
        String email = kakaoApiUtil.getKakaoMemberInfo(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", Role.USER, SocialLoginType.KAKAO)));
        member.checkLoginType(SocialLoginType.KAKAO);
        String token = tokenProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return TokenResponse.from(token);
    }
}
