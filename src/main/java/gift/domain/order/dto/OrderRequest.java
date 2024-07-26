package gift.domain.order.dto;

import gift.domain.order.entity.Order;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record OrderRequest(

    @NotNull(message = "상품을 선택해주세요.")
    Long productId,

    @NotNull(message = "옵션을 선택해주세요.")
    Long optionId,

    @Range(min = 1, max = 100000000, message = "옵션 수량은 1 이상 100,000,000 이하입니다.")
    int quantity,

    String message
) {
    public Order toOrder(User user, Product product, Option option) {
        return new Order(null, user, product, option, quantity, message);
    }
}
