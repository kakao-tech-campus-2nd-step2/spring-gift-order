package gift.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.member.domain.KakaoMember;

public record KakaoTokenResponseDto(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Integer expiresIn,
        @JsonProperty("scope") String scope) {

    public KakaoMember toKakaoMember() {
        return new KakaoMember(accessToken, tokenType, refreshToken, expiresIn, scope);
    }

}
