package gift.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoProfileResponse(
        Long id,
        KakaoAccount kakaoAccount
) {
    public static record KakaoAccount(
            String email
    ) {}
}