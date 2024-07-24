package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("connected_at") String connectedAt,
    @JsonProperty("kakao_account") KakaoAccount kakaoAccount) {

    public record KakaoAccount(
        @JsonProperty("profile") Profile profile,
        @JsonProperty("is_email_valid") boolean isEmailValid,
        @JsonProperty("is_email_verified") boolean isEmailVerified,
        @JsonProperty("email") String email) {

        public record Profile(
            @JsonProperty("nickname") String nickname) {

        }
    }
}
