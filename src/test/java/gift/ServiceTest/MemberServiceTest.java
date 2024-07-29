package gift.ServiceTest;

import gift.domain.other.Member;
import gift.domain.other.MemberRequest;
import gift.domain.other.WishList;
import gift.repository.MemberRepository;
import gift.service.JwtService;
import gift.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtService jwtService;

    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(memberRepository,jwtService);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void joinTest() {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword","김민지");
        Member member = new Member("testId","testPassword","김민지",new LinkedList<WishList>());
        Mockito.when(memberRepository.save(any(Member.class))).thenReturn(member);
        Mockito.when(memberRepository.existsById("testId")).thenReturn(false);

        memberService.join(memberRequest);
    }

    @Test
    @DisplayName("이미 존재하는 회원 추가 테스트")
    public void testJoin_ThrowsException() {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword","김민지");
        Mockito.when(memberRepository.existsById("testId")).thenReturn(true);

        assertThatThrownBy(() -> memberService.join(memberRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("이미 존재하는 회원입니다.");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLogin_Success() {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword","김민지");
        Member member = new Member("testId", "김민지", "testPassword",new LinkedList<WishList>());

        Mockito.when(memberRepository.findById(memberRequest.id())).thenReturn(Optional.of(member));
        Mockito.when(jwtService.createJWT(memberRequest.id())).thenReturn("token");

        String jwt = memberService.login(memberRequest);

        assertThat(jwt).isEqualTo("token");
    }

    @Test
    @DisplayName("잘못된 비빌번호 테스트")
    public void testLogin_wrongPassword() {
        MemberRequest memberRequest = new MemberRequest("testId", "wrongPassword","김민지");
        Member member = new Member("testId", "testPassword", "김민지",new LinkedList<WishList>());

        Mockito.when(memberRepository.findById(memberRequest.id())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.login(memberRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("로그인에 실패하였습니다. 다시 시도해주세요");
    }

    @Test
    @DisplayName("존재하지 않는 멤버 테스트")
    public void testLogin_NonExistingMember() {
        MemberRequest memberRequest = new MemberRequest("TestId", "testPassword","김민지");

        Mockito.when(memberRepository.findById(memberRequest.id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(memberRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("로그인에 실패했습니다 다시 시도해주세요");
    }

    @Test
    @DisplayName("id로 멤버 찾기 테스트")
    public void testFindById_Success() {
        Member member = new Member("testId", "testPassword", "김민지", new LinkedList<WishList>());

        Mockito.when(memberRepository.findById("testId")).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById("testId");

        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 Id로 멤버 찾기 테스트")
    public void testFindById_NotExist() {
        Mockito.when(memberRepository.findById("nonExistingId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById("nonExistingId"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당하는 회원 정보가 없습니다.");
    }
}
