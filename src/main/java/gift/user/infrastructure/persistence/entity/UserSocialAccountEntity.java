package gift.user.infrastructure.persistence.entity;

import gift.core.domain.authentication.OAuthType;
import gift.core.domain.user.UserSocialAccount;
import jakarta.persistence.*;

@Entity
@Table(name = "user_social_account")
@IdClass(UserSocialAccountEntityKey.class)
public class UserSocialAccountEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private OAuthType provider;

    @Id
    @Column(name = "social_id")
    private String socialId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public static UserSocialAccountEntity of(Long userId, UserSocialAccount account) {
        return new UserSocialAccountEntity(userId, account.provider(), account.socialId());
    }

    protected UserSocialAccountEntity() {
    }

    protected UserSocialAccountEntity(Long userId, OAuthType provider, String socialId) {
        this.userId = userId;
        this.provider = provider;
        this.socialId = socialId;
    }

    public OAuthType getProvider() {
        return provider;
    }

    public String getSocialId() {
        return socialId;
    }

    public Long getUserId() {
        return userId;
    }
}
