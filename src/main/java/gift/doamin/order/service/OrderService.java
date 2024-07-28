package gift.doamin.order.service;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import gift.doamin.order.repository.OrderRepository;
import gift.doamin.product.entity.Option;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.entity.User;
import gift.doamin.wishlist.entity.Wish;
import gift.doamin.wishlist.repository.JpaWishListRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final JpaWishListRepository jpaWishListRepository;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
        JpaWishListRepository jpaWishListRepository) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.jpaWishListRepository = jpaWishListRepository;
    }

    @Transactional
    public OrderResponse makeOrder(User user, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(() ->
            new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));
        Order order = new Order(user, user, option, orderRequest.getQuantity(),
            orderRequest.getMessage());
        order = orderRepository.save(order);

        option.subtract(orderRequest.getQuantity());

        subtractWishList(user, option, orderRequest.getQuantity());

        return new OrderResponse(order);
    }

    @Transactional
    public void subtractWishList(User user, Option option, Integer quantity) {
        jpaWishListRepository.findByUserIdAndProductId(user.getId(), option.getProduct().getId())
            .ifPresent(wish -> {
                try {
                    wish.subtract(quantity);
                } catch (IllegalArgumentException e) {
                    jpaWishListRepository.delete(wish);
                }
            });
    }

}
