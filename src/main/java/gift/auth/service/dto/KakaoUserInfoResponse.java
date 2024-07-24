package gift.auth.service.dto;

import java.time.LocalDateTime;

public record KakaoUserInfoResponse(
        Long id,
        LocalDateTime connected_at
) {
}
