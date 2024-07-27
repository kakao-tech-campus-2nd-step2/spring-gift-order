package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequest(

    @NotEmpty(message = "주문하실 상품을 한 개 이상 선택해주세요.")
    List<@Valid OrderItemRequest> orderItems,

    String message,

    int totalPrice
) {
    public Order toOrder(User user) {
        return new Order(null, user, message, totalPrice);
    }
}
