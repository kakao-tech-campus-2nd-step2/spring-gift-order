package gift.repository;

import gift.domain.KakaoMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember,Long> {
    Optional<KakaoMember> findByAccessToken(String accessToken);
    boolean existsByAccessToken(String accessToken);
}
