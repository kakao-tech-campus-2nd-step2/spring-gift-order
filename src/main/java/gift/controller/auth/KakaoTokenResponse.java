package gift.controller.auth;

public class KakaoTokenResponse {

    String accessToken;
    String tokenType;
    String refreshToken;
    String expiresIn;
    String refreshTokenExpiresIn;

    public KakaoTokenResponse(String accessToken, String tokenType, String refreshToken,
        String expiresIn, String refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }
}
