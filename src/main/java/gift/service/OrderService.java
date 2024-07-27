package gift.service;

import gift.entity.Option;
import gift.repository.OptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import gift.entity.Order;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;

    public OrderService(OrderRepository orderRepository, WishRepository wishRepository, OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // Option 엔티티 조회
        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new RuntimeException("옵션을 찾을 수 없습니다."));

        // 주문 생성 및 재고 차감 로직
        Order order = new Order.Builder(option, request.getQuantity())
                .message(request.getMessage())
                .build();
        orderRepository.save(order);

        // 위시 리스트에서 삭제
        wishRepository.deleteByOption(option);

        // 응답 생성
        return new OrderResponse.Builder()
                .id(order.getId())
                .optionId(order.getOption().getId())
                .quantity(order.getQuantity())
                .orderDateTime(order.getOrderDateTime())
                .message(order.getMessage())
                .build();
    }
}