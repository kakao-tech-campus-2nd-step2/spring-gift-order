package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OptionService optionService;

    public OrderService(OrderRepository orderRepository, OptionService optionService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    public OrderResponseDto placeOrderWithMessage(OrderRequestDto orderRequestDto) {

        var option = optionService.getOptionById(orderRequestDto.getOptionId());

        return OrderResponseDto.fromEntity(orderRepository.save(new Order(option, orderRequestDto.getQuantity(), LocalDateTime.now(), orderRequestDto.getMessage())));
    }

}
