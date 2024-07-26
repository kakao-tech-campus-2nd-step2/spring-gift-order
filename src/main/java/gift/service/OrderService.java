package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;

    public OrderService(OrderRepository orderRepository, OptionService optionService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        optionService.decreaseOptionQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        Order order = new Order.Builder()
                .optionId(orderRequest.getOptionId())
                .quantity(orderRequest.getQuantity())
                .orderTime(orderRequest.getOrderTime())
                .message(orderRequest.getMessage())
                .build();
        order = orderRepository.save(order);

        return new OrderResponse.Builder()
                .id(order.getId())
                .optionId(order.getOptionId())
                .quantity(order.getQuantity())
                .orderTime(order.getOrderTime())
                .message(order.getMessage())
                .build();
    }
}
