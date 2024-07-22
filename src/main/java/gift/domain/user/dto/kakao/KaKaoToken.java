package gift.domain.user.dto.kakao;

import jakarta.validation.constraints.NotBlank;

public record KaKaoToken(
    @NotBlank
    String accessToken,
    @NotBlank
    String refreshToken
) {

}
