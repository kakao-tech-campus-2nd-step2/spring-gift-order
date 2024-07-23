package gift.core.domain.user;

import gift.core.domain.authentication.OAuthType;

public class UserSocialAccount extends UserAccount {
    public UserSocialAccount(OAuthType provider, String socialId) {
        super(provider.name(), socialId);
    }

    public OAuthType provider() {
        return OAuthType.valueOf(principal());
    }

    public String socialId() {
        return credentials();
    }
}
