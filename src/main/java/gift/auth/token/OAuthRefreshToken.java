package gift.auth.token;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refreshToken")
public class OAuthRefreshToken {
    @Id
    private String username;
    private String tokenType;
    private String refreshToken;
    private String issuer;

    @TimeToLive
    private long expiresIn;

    public OAuthRefreshToken(String username, String tokenType, String refreshToken, String issuer, long expiresIn) {
        this.username = username;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.issuer = issuer;
        this.expiresIn = expiresIn;
    }
}
