package gift.service;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse saveOrder(OrderRequest orderRequest) {
        Order order = new Order(orderRequest.optionId(), orderRequest.quantity(), orderRequest.message());
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOptionId(),
                savedOrder.getQuantity(),
                savedOrder.getOrderDateTime(),
                savedOrder.getMessage()
        );
    }
}
