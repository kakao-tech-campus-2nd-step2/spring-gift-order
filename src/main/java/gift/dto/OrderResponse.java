package gift.dto;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long optionId, Integer quantity, LocalDateTime localDateTime,
                            String message) {

}
