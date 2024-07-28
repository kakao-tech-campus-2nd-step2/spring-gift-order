package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponse {
    
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private int refreshTokenExpiresIn;

    @JsonProperty("expires_in")
    private int expiresIn;
}
