package gift.api.order.dto;

import gift.api.order.Order;
import java.sql.Timestamp;

public record OrderResponse(
    Long id,
    Long optionId,
    Integer quantity,
    Timestamp orderDateTime,
    String message
) {

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(),
            order.getOption().getId(),
            order.getOption().getQuantity(),
            order.getOrderDateTime(),
            order.getMessage());
    }
}
