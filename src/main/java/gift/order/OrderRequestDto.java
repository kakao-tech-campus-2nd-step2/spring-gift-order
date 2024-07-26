package gift.order;

import gift.order.domain.OptionCount;
import gift.order.domain.OptionName;

public record OrderRequestDto(OptionName name, OptionCount count, Long productId) {
    public OrderServiceDto toOrderServiceDto() {
        return new OrderServiceDto(null, name, count, productId);
    }

    public OrderServiceDto toOrderServiceDto(Long id) {
        return new OrderServiceDto(id, name, count, productId);
    }
}
