package gift.order;

import gift.order.domain.Order;

import java.util.List;
import java.util.stream.Collectors;

public record OrderListResponseDto(List<OrderResponseDto> orderResponseDtos) {
    public static OrderListResponseDto orderListToOptionListResponseDto(List<Order> orders) {
        List<OrderResponseDto> newOrderResponseDtos = orders.stream()
                .map(order -> new OrderResponseDto(order.getId(), order.getCount(), order.getMessage(), order.getOption().getId()))
                .collect(Collectors.toList());

        return new OrderListResponseDto(newOrderResponseDtos);
    }
}
