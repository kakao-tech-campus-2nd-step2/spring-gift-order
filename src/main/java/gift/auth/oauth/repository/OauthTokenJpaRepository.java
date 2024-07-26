package gift.auth.oauth.repository;

import gift.auth.AuthProvider;
import gift.auth.oauth.entity.OauthToken;
import gift.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenJpaRepository extends JpaRepository<OauthToken, Long> {

    Optional<OauthToken> findByUserAndProvider(User user, AuthProvider provider);
}
