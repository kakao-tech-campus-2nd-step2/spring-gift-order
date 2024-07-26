package gift.service;

import gift.DTO.order.OrderRequest;
import gift.DTO.order.OrderResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final OptionService optionService;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        OptionService optionService
        ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
    }

    @Transactional
    public OrderResponse order(OrderRequest orderRequest) {
        Long optionId = orderRequest.optionId();
        Long quantity = orderRequest.quantity();

        Option option= optionRepository.findById(optionId)
                            .orElseThrow(() -> new RuntimeException("No such option" + optionId));
        Order newOrder = new Order(quantity,
                                    orderRequest.message(),
                                    LocalDateTime.now(),
                                    option);
        Order savedOrder = orderRepository.save(newOrder);
        optionService.decrementOptionQuantity(optionId, quantity);

        return OrderResponse.fromEntity(savedOrder);
    }
}
