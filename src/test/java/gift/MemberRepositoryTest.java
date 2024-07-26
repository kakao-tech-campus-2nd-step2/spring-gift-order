package gift;

import gift.entity.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {
    private final MemberRepository users;

    public MemberRepositoryTest(MemberRepository memberRepository) {
        users = memberRepository;
    }

    @Test
    public void save(Member member) {
        users.save(member);
    }

}
//
//org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter [gift.repository.MemberRepository memberRepository] in constructor [public gift.MemberRepositoryTest(gift.repository.MemberRepository)].
//
//at java.base/java.util.Optional.orElseGet(Optional.java:364)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
