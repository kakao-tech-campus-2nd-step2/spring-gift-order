package gift.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(

    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    String expiresIn,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("refresh_token_expires_in")
    String refreshTokenExpiresIn
){
    
}
