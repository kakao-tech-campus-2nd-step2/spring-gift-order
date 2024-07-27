package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String accessToken) {
        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        Order order = new Order(option, request.getQuantity(), request.getMessage());
        orderRepository.save(order);

        OrderResponse response = new OrderResponse(
                order.getId(),
                option.getProduct().getName(),
                order.getQuantity(),
                order.getUserName()
        );

        kakaoService.sendKakaoMessage(response, accessToken);

        return response;
    }
}