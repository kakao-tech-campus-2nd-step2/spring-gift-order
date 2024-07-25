package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Option;
import gift.model.Order;
import gift.model.OrderDTO;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Option option = optionRepository.findById(orderDTO.optionId())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.OPTION_NOT_FOUND, orderDTO.optionId()));

        option.subtract(orderDTO.quantity());
        Order order = new Order(orderDTO.optionId(), orderDTO.quantity(), orderDTO.message());
        order.updateOrderDate(LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return convertToDTO(orderRepository.save(order));
    }

    public void sendOrderMessage(OrderDTO orderDTO) {

    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDate(), order.getMessage());
    }
}
