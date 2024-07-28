package gift.service;

import gift.DTO.order.OrderRequest;
import gift.DTO.order.OrderResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;

    public OrderService(
        OrderRepository orderRepository,
        OptionService optionService
        ) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    @Transactional
    public OrderResponse makeOrder(OrderRequest orderRequest) {
        Long optionId = orderRequest.optionId();
        Long quantity = orderRequest.quantity();

        Option option= optionService.getOptionById(optionId);
        optionService.decrementOptionQuantity(optionId, quantity);
        Order newOrder = new Order(quantity,
                                    orderRequest.message(),
                                    LocalDateTime.now(),
                                    option);
        Order savedOrder = orderRepository.save(newOrder);

        return OrderResponse.fromEntity(savedOrder);
    }
}
