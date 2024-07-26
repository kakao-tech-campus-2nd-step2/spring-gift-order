package gift.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDto {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    private String scope;

    public KakaoTokenResponseDto() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}