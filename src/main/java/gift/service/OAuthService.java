package gift.service;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import gift.repository.KakaoTokenRepository;
import gift.service.dto.KakaoTokenDto;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final KakaoProperties properties;
    private final KakaoApiCaller kakaoApiCaller;
    private final KakaoTokenRepository kakaoTokenRepository;

    public OAuthService(MemberRepository memberRepository, JwtProvider jwtProvider, KakaoProperties kakaoProperties, KakaoApiCaller kakaoApiCaller, KakaoTokenRepository kakaoTokenRepository) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.properties = kakaoProperties;
        this.kakaoApiCaller = kakaoApiCaller;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public TokenResponse signIn(String code) {
        return signIn(code, properties.redirectUrl());
    }

    public TokenResponse signIn(String code, String redirectUrl) {
        KakaoTokenDto kakaoTokenDto = kakaoApiCaller.getKakaoAccessToken(code, redirectUrl);
        String email = kakaoApiCaller.getKakaoMemberInfo(kakaoTokenDto.access_token());
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", Role.USER, SocialLoginType.KAKAO)));
        member.checkLoginType(SocialLoginType.KAKAO);

        kakaoTokenRepository.saveToken(member.getId(), kakaoTokenDto);
        String token = jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return TokenResponse.from(token);
    }

    public void signOut(Long memberId) {
        String accessToken = refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.signOutKakao(accessToken);
        kakaoTokenRepository.deleteAccessToken(memberId);
        kakaoTokenRepository.deleteRefreshToken(memberId);
    }

    private String refreshIfAccessTokenExpired(Long memberId) {
        if (kakaoTokenRepository.existsAccessToken(memberId)) {
            return kakaoTokenRepository.getAccessToken(memberId);
        }
        if(kakaoTokenRepository.existsRefreshToken(memberId)) {
            String refreshToken = kakaoTokenRepository.getRefreshToken(memberId);
            KakaoTokenDto tokenDto = kakaoApiCaller.refreshAccessToken(refreshToken);
            return tokenDto.access_token();
        }
        throw new AuthenticationException("Login has expired");
    }
}
