package gift.order;

import gift.order.domain.Order;
import gift.order.domain.OptionCount;
import gift.order.domain.OptionName;

public record OrderResponseDto(Long id, OptionName name, OptionCount count, Long productId) {
    public static OrderResponseDto orderToOrderResponseDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getName(), order.getCount(), order.getProduct().getId());
    }
}
