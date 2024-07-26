package gift.domain.repository;

import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoOauthMemberRepository extends JpaRepository<KakaoOauthMember, Long> {

    Optional<KakaoOauthMember> findByMember(Member member);
}
