package gift.users.kakao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUserIdAndSns(long userId, String sns);

    boolean existsByUserId(long userId);
}
