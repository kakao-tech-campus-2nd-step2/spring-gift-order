package gift.product.dto.auth;

public record AccessAndRefreshToken(
    String accessToken,
    String refreshToken
) {

}
