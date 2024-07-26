package gift.service;

import org.springframework.stereotype.Service;
import gift.entity.Order;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, WishRepository wishRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.kakaoService = kakaoService;
    }

    public OrderResponse createOrder(OrderRequest request) {
        // 주문 생성 및 재고 차감 로직
        Order order = new Order(request.getOptionId(), request.getQuantity(), request.getMessage());
        orderRepository.save(order);

        // 위시 리스트에서 삭제
        wishRepository.deleteByOptionId(request.getOptionId());

        // 응답 생성
        OrderResponse response = new OrderResponse.Builder()
                .id(order.getId())
                .optionId(order.getOptionId())
                .quantity(order.getQuantity())
                .orderDateTime(order.getOrderDateTime())
                .message(order.getMessage())
                .build();

        // 카카오톡 메시지 전송
        kakaoService.sendKakaoMessage(response);

        return response;
    }
}