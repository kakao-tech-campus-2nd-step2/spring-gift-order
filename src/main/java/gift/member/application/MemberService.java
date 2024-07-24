package gift.member.application;

import gift.auth.application.KakaoClient;
import gift.auth.dto.AuthResponse;
import gift.auth.util.KakaoAuthUtil;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberDto;
import gift.member.entity.Member;
import gift.member.util.MemberMapper;
import org.springframework.stereotype.Service;

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
        String accessToken = kakaoClient.getAccessToken(code);
        Long kakaoUserId = kakaoClient.getUserId(accessToken);
        String email = "kakao_user" + kakaoUserId + "@kakao.com";

        MemberDto member = memberRepository.findByEmail(email)
                .map(MemberMapper::toDto)
                .orElseGet(() -> {
                    String password = KakaoAuthUtil.generateTemporaryPassword();
                    MemberDto newMember = new MemberDto(email, password);
                    registerMember(newMember);
                    return newMember;
                });

        return authenticate(member);
    }

}
