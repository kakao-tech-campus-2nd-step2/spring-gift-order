package gift.domain;

import gift.domain.other.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회원 관련 테스트")
class MemberTest {

    @DisplayName("비밀번호 검사를 요청했을 때, 비밀번호가 일치하면 아무런 일이 벌어지지 않는다.")
    @Test
    void validatePasswordTest() {
        String samePassword = "testPassword";
        Member member = new Member("testId", "김민지", samePassword, null);
        member.validatePassword(samePassword);

        assertThatCode(() -> member.validatePassword(samePassword))
                .doesNotThrowAnyException();
    }

    @DisplayName("비밀번호 검사를 요청했을 때, 비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void validatePasswordTest2() {
        String password = "testPassword";
        String differentPassword = "differentPassword";
        Member member = new Member("testId", "김민지", password, null);

        assertThatThrownBy(() -> member.validatePassword(differentPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }
}