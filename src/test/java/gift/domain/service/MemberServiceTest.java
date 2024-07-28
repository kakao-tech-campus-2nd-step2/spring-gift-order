package gift.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import gift.domain.dto.request.member.LocalMemberRequest;
import gift.domain.dto.response.MemberResponse;
import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import gift.domain.exception.conflict.MemberAlreadyExistsException;
import gift.domain.exception.forbidden.MemberIncorrectLoginInfoException;
import gift.domain.repository.MemberRepository;
import gift.domain.service.member.DerivedMemberService;
import gift.domain.service.member.LocalMemberService;
import gift.domain.service.member.MemberService;
import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import gift.global.util.HashUtil;
import gift.global.util.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

//TODO: 새로 추가된 DerivedMemberFactory에 대한 stubbing 추가해서 테스트가 성공하도록 고치기
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DerivedMemberService.Factory factory;

    @Mock
    private LocalMemberService localMemberService;

    @Mock
    private JwtUtil jwtUtil;

    private LocalMemberRequest memberRequest;
    private Member member;
    private LocalMember localMember;
    private String createdToken;

    @BeforeEach
    void setUp() {
        createdToken = "token";
        memberRequest = new LocalMemberRequest("test@example.com", "password");
        member = new Member("test@example.com", Permission.MEMBER, Type.LOCAL); //id가 null이 들어가게 됨
        ReflectionTestUtils.setField(member, "id", 1L);//리플렉션 툴을 통해 아이디를 채운다.
        localMember = new LocalMember(HashUtil.hashCode("password"), member);
        ReflectionTestUtils.setField(localMember, "id", 1L);
    }

    @Test
    @DisplayName("[UnitTest] 회원가입 - 처음 가입하는 경우")
    void registerMember() {
        //given (mocking)
        given(memberRepository.findByEmail(memberRequest.getEmail()))
            .willReturn(Optional.empty()); //기존 회원 확인하는 과정에서 Optional.empty() 반환(mock 설정)
        given(memberRepository.save(any(Member.class)))
            .willReturn(member); //저장된 회원 정보 반환(mock 설정)
        given(jwtUtil.generateToken(any(Member.class)))
            .willReturn(createdToken); //JWT 생성 과정(mock 설정)

        //when
        MemberResponse response = memberService.registerMember(memberRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(createdToken);
        // memberRepository.save() 호출 검증
        then(memberRepository).should(times(1)).save(any(Member.class));
        // memberRepository.findByEmail() 호출 검증
        then(memberRepository).should(times(1)).findByEmail(eq(memberRequest.getEmail()));
        // jwtUtil.generateToken() 호출 검증
        then(jwtUtil).should(times(1)).generateToken(any(Member.class));
    }

    @Test
    @DisplayName("[UnitTest/Fail] 회원가입: 이미 회원가입 된 상황")
    void registerMember_AlreadyExistsMember() {
        //given: 이미 존재하는 회원(mock 설정)
        given(memberRepository.findByEmail(memberRequest.getEmail())).willReturn(Optional.of(member));

        //when: 예외 발생 검증
        assertThatThrownBy(() -> memberService.registerMember(memberRequest))
            .isInstanceOf(MemberAlreadyExistsException.class);

        //then
        // memberRepository.save() 호출 검증하지 않음
        then(memberRepository).should(never()).save(any(Member.class));
        // jwtUtil.generateToken() 호출 검증하지 않음
        then(jwtUtil).should(never()).generateToken(any(Member.class));
    }

    @Test
    @DisplayName("[UnitTest] 로그인: 아이디 존재하고 비밀번호 일치하는 경우")
    void loginMember() {
        //given
        given(memberRepository.findByEmail(memberRequest.getEmail())).willReturn(Optional.of(member));
        given(jwtUtil.generateToken(any(Member.class))).willReturn(createdToken);

        //when
        MemberResponse response = memberService.loginMember(memberRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(createdToken);
        then(memberRepository).should(times(1)).findByEmail(eq(memberRequest.getEmail()));
        then(jwtUtil).should(times(1)).generateToken(eq(member));
    }

    @Test
    @DisplayName("[UnitTest/Fail] 로그인: 아이디가 존재하지 않은 경우")
    void loginMember_IdNotExists() {
        //given
        given(memberRepository.findByEmail(eq(memberRequest.getEmail()))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> memberService.loginMember(memberRequest))
            .isInstanceOf(MemberIncorrectLoginInfoException.class);

        then(jwtUtil).should(never()).generateToken(any(Member.class));
    }

    @Test
    @DisplayName("[UnitTest/Fail] 로그인: 비밀번호가 일치하지 않은 경우")
    void loginMember_IncorrectPassword() {
        //given
        LocalMemberRequest wrongPasswordRequest = new LocalMemberRequest(memberRequest.getEmail(), "incorrectPassword");
        given(memberRepository.findByEmail(eq(wrongPasswordRequest.getEmail()))).willReturn(Optional.of(member));

        //when & then
        assertThatThrownBy(() -> memberService.loginMember(wrongPasswordRequest))
            .isInstanceOf(MemberIncorrectLoginInfoException.class);

        then(jwtUtil).should(never()).generateToken(any(Member.class));
    }

    @Test
    @DisplayName("[UnitTest/Fail] 로그인: 아이디 존재하지 않고 비밀번호가 일치하지 않은 경우")
    void loginMember_IdNotExistsAndIncorrectPassword() {
        //given
        LocalMemberRequest incorrectRequest = new LocalMemberRequest("notExist@example.com", "incorrectPassword");
        given(memberRepository.findByEmail(eq(incorrectRequest.getEmail()))).willReturn(Optional.of(member));

        //when & then
        assertThatThrownBy(() -> memberService.loginMember(incorrectRequest))
            .isInstanceOf(MemberIncorrectLoginInfoException.class);

        then(jwtUtil).should(never()).generateToken(any(Member.class));
    }
}
