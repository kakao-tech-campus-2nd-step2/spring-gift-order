package gift.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoAuth {
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private String refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;

    public KakaoAuth() {
    }

    public KakaoAuth(String tokenType, String accessToken, String expiresIn, String refreshToken, String refreshTokenExpiresIn, String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "KakaoAuth{" +
                "tokenType='" + tokenType + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenExpiresIn='" + refreshTokenExpiresIn + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
