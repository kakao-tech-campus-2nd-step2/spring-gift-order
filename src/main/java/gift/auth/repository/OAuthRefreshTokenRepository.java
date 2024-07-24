package gift.auth.repository;

import gift.auth.token.OAuthRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface OAuthRefreshTokenRepository extends CrudRepository<OAuthRefreshToken, Long> {
}
