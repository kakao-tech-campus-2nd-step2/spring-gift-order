package gift.order.repository;

import gift.order.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJPARepository extends JpaRepository<Token, Long> {
}
