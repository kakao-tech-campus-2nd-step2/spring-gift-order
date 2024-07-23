package gift.repository;

import gift.common.enums.Role;
import gift.config.JpaConfig;
import gift.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByEmailAndPassword() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        save(email, password, role);

        // when
        Member actual = memberRepository.findByEmailAndPassword(email, password).orElse(null);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getPassword()).isEqualTo(password);
        assertThat(actual.getRole()).isEqualTo(role);
    }

    @Test
    void existsByEmail() {
        // given
        String email = "test@gmail.com";
        String password = "password";
        Role role = Role.USER;
        save(email, password, role);

        // when
        boolean actual = memberRepository.existsByEmail(email);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("email이 없을 경우 저장 테스트[성공]")
    void saveIfEntityNotExist() {
        // given
        String email = "test@gmail.com";

        // when
        Member member = memberRepository.findByEmail(email)
                .orElse(memberRepository.save(new Member(email, "", Role.USER)));

        // then
        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    void save(String email, String password, Role role) {
        // given
        Member member = new Member(email, password, role);

        // when
        Member actual = memberRepository.save(member);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getRole()).isEqualTo(role);
    }

}