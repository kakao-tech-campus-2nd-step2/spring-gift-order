package gift.order;

import gift.exception.NotFoundOption;
import gift.option.Option;
import gift.option.OptionRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Option option = optionRepository.findById(orderRequestDto.optionId())
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));

        Order order = new Order(option, orderRequestDto.quantity(), orderRequestDto.message());

        orderRepository.save(order);

        return new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

}
