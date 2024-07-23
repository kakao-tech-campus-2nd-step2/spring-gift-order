package gift.service;

import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public OrderResponse save(OrderRequest orderRequest){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String orderDateTime = now.format(formatter);
        Order order = new Order(
                orderRequest.getOptionId(),
                orderRequest.getQuantity(),
                orderRequest.getMessage(),
                orderDateTime
        );

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }
    @Transactional
    public void order(OrderRequest orderRequest){
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(NoSuchFieldError::new);
        Long before = option.getQuantity();
        option.subtract(orderRequest.getQuantity());
        Long after = option.getQuantity();
        if(!before.equals(after))
            save(orderRequest);
    }
}
