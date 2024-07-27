package gift.domain.order.service;

import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.service.OptionManager;
import gift.domain.user.entity.User;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.exception.InvalidOptionInfoException;
import gift.exception.InvalidProductInfoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final OptionManager optionManager;
    private final WishlistJpaRepository wishlistJpaRepository;

    public OrderService(
        OrderJpaRepository orderJpaRepository,
        ProductJpaRepository productJpaRepository,
        OptionManager optionManager,
        WishlistJpaRepository wishlistJpaRepository
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.optionManager = optionManager;
        this.wishlistJpaRepository = wishlistJpaRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest, User user) {
        Long productId = orderRequest.productId();
        Long optionId = orderRequest.optionId();
        int quantity = orderRequest.quantity();

        Product product = productJpaRepository.findById(productId)
            .orElseThrow(() -> new InvalidProductInfoException("error.invalid.product.id"));

        if (!product.hasOption(optionId)) {
            throw new InvalidOptionInfoException("error.invalid.option.id");
        }
        Option option = optionManager.subtractQuantity(optionId, quantity);

        wishlistJpaRepository.deleteByUserAndProduct(user, product);

        Order order = orderRequest.toOrder(user, product, option);
        Order savedOrder = orderJpaRepository.save(order);
        return OrderResponse.from(savedOrder);
    }
}
