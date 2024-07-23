package gift.oauth.business.dto;

public class OauthToken {
    public record Kakao (
        String token_type,
        String access_token,
        Long expires_in,
        String refresh_token,
        Long refresh_token_expires_in,
        String scope
    ) {}

}
