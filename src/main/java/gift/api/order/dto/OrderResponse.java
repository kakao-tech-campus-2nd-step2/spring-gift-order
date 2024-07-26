package gift.api.order.dto;

import java.sql.Timestamp;

public record OrderResponse(
    Long id,
    Long optionId,
    Integer quantity,
    Timestamp orderDateTime,
    String message
) {

    public static OrderResponse of(Long id,
                                Long optionId,
                                Integer quantity,
                                Timestamp orderDateTime,
                                String message) {
        return new OrderResponse(id, optionId, quantity, orderDateTime, message);
    }
}
