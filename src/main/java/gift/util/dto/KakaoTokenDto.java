package gift.util.dto;

public record KakaoTokenDto(
        String access_token,
        String token_type,
        String refresh_token,
        Long expires_in,
        String scope,
        Long refresh_token_expires_in
) {
}
