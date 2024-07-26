package gift.repository;

import gift.domain.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    KakaoToken findByMemberId(Long memberId);
}
