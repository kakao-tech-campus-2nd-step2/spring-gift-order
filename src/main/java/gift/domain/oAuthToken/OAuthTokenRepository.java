package gift.domain.oAuthToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {

    OAuthToken findByMemberEmail(String memberEmail);
}
