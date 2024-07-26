package gift.order.service;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.order.dto.request.OrderRequest;
import gift.order.dto.response.OrderResponse;
import gift.order.entity.Order;
import gift.order.repository.OrderJpaRepository;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionJpaRepository;
import gift.product.option.service.OptionService;
import gift.user.entity.User;
import gift.user.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    private final OptionService optionService;
    private final OptionJpaRepository optionRepository;
    private final UserJpaRepository userRepository;
    private final OrderJpaRepository orderRepository;


    public OrderService(OptionService optionService, OptionJpaRepository optionRepository,
        UserJpaRepository userRepository, OrderJpaRepository orderRepository) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        optionService.subtractOptionQuantity(request.optionId(), request.quantity());
        Option option = optionRepository.findById(request.optionId())
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.getWishes()
            .removeIf(wish -> wish.getProduct().equals(option.getProduct()));

        Order order = new Order(request.optionId(), request.quantity(), request.message());

        Order saved = orderRepository.save(order);

        return OrderResponse.from(saved);
    }
}
