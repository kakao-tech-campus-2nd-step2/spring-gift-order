package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.order.entity.OrderItem;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record OrderItemRequest(

    @NotNull(message = "상품을 선택해주세요.")
    Long productId,

    @NotNull(message = "옵션을 선택해주세요.")
    Long optionId,

    @Range(min = 1, max = 100000000, message = "옵션 수량은 1 이상 100,000,000 이하입니다.")
    int quantity
) {
    public OrderItem toOrderItem(Order order, Product product, Option option) {
        return new OrderItem(null, order, product, option, quantity);
    }
}
