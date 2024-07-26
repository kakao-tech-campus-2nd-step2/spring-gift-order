package gift.order;

import gift.order.domain.Order;
import gift.order.domain.OptionCount;
import gift.order.domain.OptionName;
import gift.product.domain.Product;

public record OrderServiceDto(Long id, OptionName name, OptionCount count, Long productId) {
    public Order toOrder(Product product) {
        return new Order(id, name, count, product);
    }
}
