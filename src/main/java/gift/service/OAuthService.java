package gift.service;

import gift.common.enums.Role;
import gift.common.enums.SocialLoginType;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.controller.dto.response.TokenResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import gift.util.KakaoApiUtil;
import gift.util.KakaoTokenUtil;
import gift.util.dto.KakaoTokenDto;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final KakaoProperties properties;
    private final KakaoApiUtil kakaoApiUtil;
    private final KakaoTokenUtil kakaoTokenUtil;

    public OAuthService(MemberRepository memberRepository, JwtProvider jwtProvider, KakaoProperties kakaoProperties, KakaoApiUtil kakaoApiUtil, KakaoTokenUtil kakaoTokenUtil) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.properties = kakaoProperties;
        this.kakaoApiUtil = kakaoApiUtil;
        this.kakaoTokenUtil = kakaoTokenUtil;
    }

    public TokenResponse signIn(String code) {
        return signIn(code, properties.redirectUrl());
    }

    public TokenResponse signIn(String code, String redirectUrl) {
        KakaoTokenDto kakaoTokenDto = kakaoApiUtil.getKakaoAccessToken(code, redirectUrl);
        String email = kakaoApiUtil.getKakaoMemberInfo(kakaoTokenDto.access_token());
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", Role.USER, SocialLoginType.KAKAO)));
        member.checkLoginType(SocialLoginType.KAKAO);

        kakaoTokenUtil.saveToken(member.getId(), kakaoTokenDto);
        String token = jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole());
        return TokenResponse.from(token);
    }

    public void signOut(Long memberId) {
        String accessToken = refreshIfAccessTokenExpired(memberId);
        kakaoApiUtil.signOutKakao(accessToken);
        kakaoTokenUtil.deleteAccessToken(memberId);
        kakaoTokenUtil.deleteRefreshToken(memberId);
    }

    private String refreshIfAccessTokenExpired(Long memberId) {
        if (kakaoTokenUtil.existsAccessToken(memberId)) {
            return kakaoTokenUtil.getAccessToken(memberId);
        }
        if(kakaoTokenUtil.existsRefreshToken(memberId)) {
            String refreshToken = kakaoTokenUtil.getRefreshToken(memberId);
            KakaoTokenDto tokenDto = kakaoApiUtil.refreshAccessToken(refreshToken);
            return tokenDto.access_token();
        }
        throw new AuthenticationException("Login has expired");
    }


}
