package gift.users.kakao;

public record KakaoTokenDTO(String tokenType, String accessToken, int expiresIn,
                            String refreshToken, int refreshTokenExpiresIn) {

}
