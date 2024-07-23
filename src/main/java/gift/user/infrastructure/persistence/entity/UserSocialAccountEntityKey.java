package gift.user.infrastructure.persistence.entity;

import gift.core.domain.authentication.OAuthType;
import gift.core.domain.user.UserSocialAccount;

public class UserSocialAccountEntityKey {
    private final OAuthType provider = OAuthType.KAKAO;
    private final String socialId = "";

    public static UserSocialAccountEntityKey fromDomain(UserSocialAccount account) {
        return new UserSocialAccountEntityKey(account.provider(), account.socialId());
    }

    public UserSocialAccountEntityKey() {
    }

    public UserSocialAccountEntityKey(OAuthType provider, String socialId) {
    }

    public OAuthType getProvider() {
        return provider;
    }

    public String getSocialId() {
        return socialId;
    }
}
