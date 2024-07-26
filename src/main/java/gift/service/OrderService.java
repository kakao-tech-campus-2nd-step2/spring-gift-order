package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.OrderRepository;
import gift.vo.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;

    public OrderService(OrderRepository orderRepository, OptionService optionService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    public Order createOrder(Long memberId, OrderRequestDto orderRequestDto) {
        optionService.subtractOptionQuantity(orderRequestDto.optionId(), orderRequestDto.quantity());
        return orderRepository.save(orderRequestDto.toOrder(memberId));
    }

}
