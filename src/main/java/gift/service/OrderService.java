package gift.service;

import gift.exception.option.NotFoundOptionsException;
import gift.model.Options;
import gift.model.Order;
import gift.repository.OptionsRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionsRepository optionsRepository;
    private final OptionsService optionsService;
    private final WishService wishService;
    private final WishRepository wishRepository;

    public OrderService(OrderRepository orderRepository, OptionsRepository optionsRepository,
        OptionsService optionsService, WishService wishService, WishRepository wishRepository) {
        this.orderRepository = orderRepository;
        this.optionsRepository = optionsRepository;
        this.optionsService = optionsService;
        this.wishService = wishService;
        this.wishRepository = wishRepository;
    }


    @Transactional
    public OrderResponse makeOrder(Long memberId, Long productId, Long optionId, Integer quantity, String message) {
        Options option = optionsRepository.findById(optionId)
            .orElseThrow(NotFoundOptionsException::new);
        Order order = new Order(memberId, option, quantity, message);

        optionsService.subtractQuantity(optionId, quantity, productId);
        wishRepository.findByMemberIdAndProductId(memberId, productId)
            .ifPresent(wish -> wishService.deleteMyWish(memberId, productId));

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder.getId(), optionId, quantity,
            savedOrder.getCreatedAt(), savedOrder.getMessage());
    }

}
