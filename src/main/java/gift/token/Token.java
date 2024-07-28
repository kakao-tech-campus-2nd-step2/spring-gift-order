package gift.token;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 송신용 DTO")
public record Token(
    @Schema(description = "액세스 토큰", example = "header.payload.signature")
    String accessToken
) {

}
