package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, WishRepository wishRepository, OptionRepository optionRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String accessToken) {
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
        OrderResponse orderResponse = new OrderResponse.Builder()
                .id(order.getId())
                .optionId(order.getOption().getId())
                .quantity(order.getQuantity())
                .orderDateTime(order.getOrderDateTime())
                .message(order.getMessage())
                .build();

        // 카카오 메시지 전송
        try {
            kakaoService.sendKakaoMessage(orderResponse, accessToken);
        } catch (Exception e) {
            throw new RuntimeException("카카오 메시지 전송 중 예외가 발생했습니다.: " + e.getMessage());
        }

        return orderResponse;
    }
}