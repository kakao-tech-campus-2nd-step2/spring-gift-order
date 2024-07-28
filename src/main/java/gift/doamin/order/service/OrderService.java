package gift.doamin.order.service;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.entity.Order;
import gift.doamin.order.repository.OrderRepository;
import gift.doamin.product.entity.Option;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse makeOrder(User user, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(() ->
            new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));
        Order order = new Order(user, user, option, orderRequest.getQuantity(),
            orderRequest.getMessage());
        order = orderRepository.save(order);

        return new OrderResponse(order);
    }

}
