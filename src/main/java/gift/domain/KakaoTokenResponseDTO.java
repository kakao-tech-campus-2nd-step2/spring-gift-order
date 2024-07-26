package gift.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDTO {
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("expires_in")
    public Integer expiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("refresh_token_expires_in")
    public Integer refreshTokenExpiresIn;
    @JsonProperty("scope")
    public String scope;

    public KakaoTokenResponseDTO() {}

    public String getAccessToken() {
        return accessToken;
    }
}