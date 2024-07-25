package gift.member.application;

import gift.auth.application.KakaoClient;
import gift.auth.dto.AuthResponse;
import gift.auth.dto.KakaoTokenResponse;
import gift.auth.util.KakaoAuthUtil;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberDto;
import gift.member.entity.Member;
import gift.member.util.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KakaoClient kakaoClient;

    public MemberService(MemberRepository memberRepository,
                         JwtUtil jwtUtil,
                         KakaoClient kakaoClient

    ) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoClient = kakaoClient;
    }

    public void registerMember(MemberDto memberDto) {
        // 사용자 계정 중복 검증
        memberRepository.findByEmail(memberDto.email())
                        .ifPresent(member -> {
                            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);
                        });

        memberRepository.save(MemberMapper.toEntity(memberDto));
    }

    public AuthResponse authenticate(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getPassword()
                   .equals(memberDto.password())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
        
        return AuthResponse.of(
                jwtUtil.generateToken(member.getId())
        );
    }

    public AuthResponse authenticate(String code) {
        KakaoTokenResponse tokenResponse = kakaoClient.getTokenResponse(code);
        Long kakaoUserId = kakaoClient.getUserId(tokenResponse.accessToken());
        String email = "kakao_user" + kakaoUserId + "@kakao.com";

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    String password = KakaoAuthUtil.generateTemporaryPassword();
                    Member newMember = new Member(
                            email,
                            password,
                            tokenResponse.accessToken(),
                            tokenResponse.refreshToken()
                    );
                    return memberRepository.save(newMember);
                });

        return AuthResponse.of(
                jwtUtil.generateToken(member.getId())
        );
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void refreshKakaoAccessToken(Long id) {
        Member member = getMemberById(id);
        KakaoTokenResponse refreshTokenResponse = kakaoClient.getRefreshTokenResponse(member.getKakaoRefreshToken());
        member.updateTokens(
                refreshTokenResponse.accessToken(),
                refreshTokenResponse.refreshToken()
        );
    }

}
