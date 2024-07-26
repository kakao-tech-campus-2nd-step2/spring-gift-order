package gift.controller.auth;

public record KakaoTokenResponse(String accessToken, String tokenType, String refreshToken,
                                 String expiresIn, String refreshTokenExpiresIn) {
}
