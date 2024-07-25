package gift.repository;

import gift.model.OAuth2AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AccessTokenRepository extends JpaRepository<OAuth2AccessToken, Long> {

}
