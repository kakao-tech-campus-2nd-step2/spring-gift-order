package gift.model;

public record KakaoTokenDTO(
    String tokenType,
    String accessToken,
    int expiresIn,
    String refreshToken,
    int refreshTokenExpiresIn

) {

}
