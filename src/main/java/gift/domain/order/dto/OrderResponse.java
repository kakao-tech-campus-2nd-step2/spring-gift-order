package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.product.dto.OptionResponse;
import gift.domain.product.dto.ProductResponse;
import gift.domain.user.dto.UserResponse;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    UserResponse userResponse,
    ProductResponse productResponse,
    OptionResponse optionResponse,
    int quantity,
    String message,
    LocalDateTime orderDateTime
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            UserResponse.from(order.getUser()),
            ProductResponse.from(order.getProduct()),
            OptionResponse.from(order.getOption()),
            order.getQuantity(),
            order.getMessage(),
            order.getOrderDateTime()
        );
    }
}
