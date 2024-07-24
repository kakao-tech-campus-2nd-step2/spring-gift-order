package gift.repository;

import gift.dto.KakaoProperties;
import gift.model.Member;
import gift.model.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByKakaoId(String kakaoId);
}