package gift.oauth;

import com.google.gson.annotations.SerializedName;

public class KakaoToken {

    private Long memberId;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("scope")
    private String scope;
    @SerializedName("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    protected KakaoToken() {
    }

    public Long getMemberId() {
        return memberId;
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

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public int getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

}
