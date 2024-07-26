package gift.service;

import gift.DTO.order.OrderRequest;
import gift.DTO.order.OrderResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    public OrderResponse order(OrderRequest orderRequest) {
        Long optionId = orderRequest.optionId();
        Option option= optionRepository.findById(optionId)
                            .orElseThrow(() -> new RuntimeException("No such option" + optionId));
        Order newOrder = new Order(orderRequest.quantity(),
                                    orderRequest.message(),
                                    LocalDateTime.now(),
                                    option);
        Order savedOrder = orderRepository.save(newOrder);
        return OrderResponse.fromEntity(savedOrder);
    }
}
