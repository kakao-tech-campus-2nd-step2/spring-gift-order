package gift.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.auth.token.OAuthAccessToken;
import gift.auth.token.OAuthRefreshToken;

public record KakaoTokenResponse(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("refresh_token_expires_in")
        Integer refreshTokenExpiresIn,

        @JsonProperty("scope")
        String scope
) {
    public String getAccessTokenWithTokenType() {
        return tokenType.concat(" ").concat(accessToken);
    }

    public OAuthAccessToken toAccessTokenFrom(Long id) {
        return new OAuthAccessToken(id, tokenType, accessToken, "kakao", expiresIn);
    }

    public OAuthRefreshToken toRefreshTokenFrom(Long id) {
        return new OAuthRefreshToken(id, tokenType, refreshToken, "kakao", refreshTokenExpiresIn);
    }
}