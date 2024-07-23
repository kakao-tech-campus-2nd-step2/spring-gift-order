package gift.user.infrastructure.persistence.repository;

import gift.core.domain.user.UserSocialAccount;
import gift.core.domain.user.UserSocialAccountRepository;
import gift.user.infrastructure.persistence.entity.UserSocialAccountEntity;
import gift.user.infrastructure.persistence.entity.UserSocialAccountEntityKey;
import org.springframework.stereotype.Repository;

@Repository
public class UserSocialAccountRepositoryImpl implements UserSocialAccountRepository {
    private final JpaUserSocialAccountRepository jpaUserSocialAccountRepository;

    public UserSocialAccountRepositoryImpl(JpaUserSocialAccountRepository jpaUserSocialAccountRepository) {
        this.jpaUserSocialAccountRepository = jpaUserSocialAccountRepository;
    }

    @Override
    public UserSocialAccount save(Long userId, UserSocialAccount userSocialAccount) {
        return null;
    }

    @Override
    public boolean exists(UserSocialAccount account) {
        return jpaUserSocialAccountRepository.existsById(UserSocialAccountEntityKey.fromDomain(account));
    }

    @Override
    public Long findUserIdBySocialAccount(UserSocialAccount account) {
        return jpaUserSocialAccountRepository
                .findById(UserSocialAccountEntityKey.fromDomain(account))
                .map(UserSocialAccountEntity::getUserId)
                .orElse(null);
    }
}
