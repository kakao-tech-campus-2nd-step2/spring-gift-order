package gift.user.infrastructure.persistence.repository;

import gift.user.infrastructure.persistence.entity.UserSocialAccountEntity;
import gift.user.infrastructure.persistence.entity.UserSocialAccountEntityKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserSocialAccountRepository extends JpaRepository<UserSocialAccountEntity, UserSocialAccountEntityKey> {

}
