package gift.order.repository;

import gift.global.MyCrudRepository;
import gift.order.domain.Order;
import gift.order.domain.OptionName;

import java.util.List;

public interface OrderRepository extends MyCrudRepository<Order, Long> {
    boolean existsById(Long id);

    List<Order> findByProductId(Long productId);

    boolean existsByName(OptionName name);
}
