package gift.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponse(
        @JsonProperty("access_token") String accessToken,        //사용자 액세스 토큰 값
        @JsonProperty("token_type") String tokenType,          //토큰타입, bearer로 고정
        @JsonProperty("refresh_token") String refreshToken,       //사용자 리프레시 토큰 값
        @JsonProperty("expires_in") int expiresIn,             //액세스 토큰과 ID 토큰의 만료 시간(초)
        @JsonProperty("refresh_token_expires_in") int refreshTokenExpiresIn  //리프레시 토큰 만료 시간(초)
) {
}
