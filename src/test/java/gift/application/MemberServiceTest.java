package gift.application;

import gift.auth.application.KakaoClient;
import gift.auth.dto.AuthResponse;
import gift.auth.dto.KakaoTokenResponse;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.member.application.MemberService;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberDto;
import gift.member.entity.Member;
import gift.member.util.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testFixtures.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private KakaoClient kakaoClient;

    @Test
    @DisplayName("회원가입 서비스 테스트")
    void registerMember() {
        MemberDto memberDto = new MemberDto("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberDto);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());
        given(memberRepository.save(any()))
                .willReturn(member);

        memberService.registerMember(memberDto);

        verify(memberRepository).findByEmail(memberDto.email());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void registerMemberFailed() {
        MemberDto memberDto = new MemberDto("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberDto);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.registerMember(memberDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_ALREADY_EXISTS
                                     .getMessage());
    }

    @Test
    @DisplayName("회원 검증 서비스 테스트")
    void authenticate() {
        MemberDto memberDto = new MemberDto("test@email.com", "password");
        Member member = MemberMapper.toEntity(memberDto);
        String token = "token";
        given(jwtUtil.generateToken(any()))
                .willReturn(token);
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        AuthResponse authToken = memberService.authenticate(memberDto);

        assertThat(authToken.token()).isEqualTo(token);
    }

    @Test
    @DisplayName("존재하지 않는 회원 검증 실패 테스트")
    void authenticateMemberNotFound() {
        MemberDto memberDto = new MemberDto("test@email.com", "password");
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.authenticate(memberDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND
                                     .getMessage());
    }

    @Test
    @DisplayName("회원 비밀번호 검증 실패 테스트")
    void authenticateIncorrectPassword() {
        Member member = MemberFixture.createMember("test@email.com");
        MemberDto memberDto = new MemberDto("test@email.com", "incorrect " + member.getPassword());
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.authenticate(memberDto))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.AUTHENTICATION_FAILED
                                     .getMessage());
    }

    @Test
    @DisplayName("회원 조회 기능 테스트")
    void getMemberById() {
        Long memberId = 1L;
        Member member = MemberFixture.createMember("test@email.com");
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        Member foundMember = memberService.getMemberById(memberId);

        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("카카오 토큰 갱신 기능 테스트")
    void refreshKakaoAccessToken() {
        Long memberId = 1L;
        Member member = MemberFixture.createMember("test@email.com");
        KakaoTokenResponse response = new KakaoTokenResponse(
                "token",
                "test-token",
                3600,
                "test-refresh-token",
                3600,
                "code"
        );

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(kakaoClient.getRefreshTokenResponse(any()))
                .willReturn(response);

        memberService.refreshKakaoAccessToken(memberId);
        assertThat(member.getKakaoAccessToken()).isEqualTo(response.accessToken());
        assertThat(member.getKakaoRefreshToken()).isEqualTo(response.refreshToken());
    }

}