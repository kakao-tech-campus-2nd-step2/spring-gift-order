package gift.core.domain.user;

public interface UserSocialAccountRepository {

    UserSocialAccount save(Long userId, UserSocialAccount userSocialAccount);

    boolean exists(UserSocialAccount account);

    Long findUserIdBySocialAccount(UserSocialAccount account);

}
