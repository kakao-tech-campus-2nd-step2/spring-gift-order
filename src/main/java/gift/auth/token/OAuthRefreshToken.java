package gift.auth.token;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refreshToken")
public class OAuthRefreshToken {
    @Id
    private Long id;
    private String tokenType;
    private String refreshToken;
    private String issuer;

    @TimeToLive
    private long expiresIn;

    public OAuthRefreshToken(Long id, String tokenType, String refreshToken, String issuer, long expiresIn) {
        this.id = id;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.issuer = issuer;
        this.expiresIn = expiresIn;
    }
}
