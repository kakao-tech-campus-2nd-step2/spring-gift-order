package gift.controller.dto.response;

import gift.model.Orders;

import java.time.LocalDateTime;

public record OrderResponse(
        Long productId,
        Long optionId,
        String productName,
        String optionName,
        int price,
        int quantity,
        LocalDateTime orderDateTime,
        String message
) {
    public static OrderResponse from(Orders orders) {
        return new OrderResponse(
                orders.getProductId(), orders.getOptionId(),
                orders.getProductName(), orders.getOptionName(),
                orders.getPrice(), orders.getQuantity(),
                orders.getCreatedAt(), orders.getDescription()
        );
    }
}
