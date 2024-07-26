package gift.api.member;

import gift.global.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final MemberRepository memberRepository;

    public MemberDao(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchEntityException("member"));
    }
}
