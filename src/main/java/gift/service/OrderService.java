package gift.service;

import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.exception.option.OptionNotFoundException;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND + orderRequest.optionId()));

        option.subtractQuantity(orderRequest.quantity());
        optionRepository.save(option);

        Order order = new Order(option, orderRequest.quantity(), orderRequest.message(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
            savedOrder.getId(),
            option.getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }
}
