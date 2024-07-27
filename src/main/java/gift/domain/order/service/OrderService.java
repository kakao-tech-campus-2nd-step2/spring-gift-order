package gift.domain.order.service;

import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemManager orderItemManager;

    public OrderService(OrderJpaRepository orderJpaRepository, OrderItemManager orderItemManager) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemManager = orderItemManager;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest, User user) {
        Order order = orderRequest.toOrder(user);
        orderItemManager.create(user, order, orderRequest.orderItems());

        Order savedOrder = orderJpaRepository.save(order);
        return OrderResponse.from(savedOrder);
    }
}
