package gift.auth.oauth.repository;

import gift.auth.oauth.entity.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenJpaRepository extends JpaRepository<OauthToken, Long> {

}
