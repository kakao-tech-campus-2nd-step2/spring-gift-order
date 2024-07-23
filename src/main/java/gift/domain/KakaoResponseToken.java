package gift.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoResponseToken(
    String accessToken,
    String tokenType,
    String refreshToken,
    int expiresIn,
    int refreshTokenExpiresIn
) {

}
