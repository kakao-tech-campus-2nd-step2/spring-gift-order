package gift.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDTO {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    public String getAccessToken() {
        return accessToken;
    }
}
