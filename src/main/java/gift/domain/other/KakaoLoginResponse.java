package gift.domain.other;

public record KakaoLoginResponse(
        String access_token,
        String token_type,
        String refresh_token,
        String expires_in,
        String scope,
        String refresh_token_expires_in
) {
}
