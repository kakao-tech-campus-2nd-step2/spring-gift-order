package gift.repository.token;

import gift.model.token.KakaoToken;
import gift.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByUser(User user);
}
