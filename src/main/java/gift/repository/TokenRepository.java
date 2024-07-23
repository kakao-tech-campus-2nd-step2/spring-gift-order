package gift.repository;

import gift.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Long> {
    boolean existsByToken(String token);

    Token findByToken(String token);
}
