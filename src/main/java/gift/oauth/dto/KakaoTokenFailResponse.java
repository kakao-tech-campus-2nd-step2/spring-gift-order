package gift.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenFailResponse (

    @JsonProperty("error_code")
    String errorCode,
    @JsonProperty("error_description")
    String errorDescription
) {
}
