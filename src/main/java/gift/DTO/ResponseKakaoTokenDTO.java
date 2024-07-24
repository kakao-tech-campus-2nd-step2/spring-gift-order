package gift.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseKakaoTokenDTO {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    public ResponseKakaoTokenDTO() {
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
