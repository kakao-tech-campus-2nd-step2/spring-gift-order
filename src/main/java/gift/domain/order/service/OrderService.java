package gift.domain.order.service;

import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.service.ProductService;
import gift.domain.user.entity.User;
import gift.domain.wishlist.service.WishlistService;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ProductService productService;
    private final WishlistService wishlistService;

    public OrderService(
        OrderJpaRepository orderJpaRepository,
        ProductService productService,
        WishlistService wishlistService
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.productService = productService;
        this.wishlistService = wishlistService;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest, User user) {
        Entry<Product, Option> item = productService.buy(
            orderRequest.productId(), orderRequest.optionId(), orderRequest.quantity()
        );
        Product product = item.getKey();
        Option option = item.getValue();
        wishlistService.deleteOrderedWishItem(user, product);

        Order order = orderRequest.toOrder(user, product, option);
        Order savedOrder = orderJpaRepository.save(order);
        return OrderResponse.from(savedOrder);
    }
}
