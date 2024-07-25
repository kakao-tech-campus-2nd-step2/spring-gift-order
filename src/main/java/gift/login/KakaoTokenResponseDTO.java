package gift.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponseDTO {

    private String tokenType;

    private String accessToken;

    private int expiresIn;

    private String refreshToken;

    private int refreshTokenExpiresIn;

    public String getAccessToken() {
        return accessToken;
    }
}
