package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.dto.SendMessageRequest;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoMessageService kakaoMessageService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String accessToken) {
        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        option.subtractQuantity(request.getQuantity());
        optionRepository.save(option);

        Order order = new Order(option, request.getQuantity(), LocalDateTime.now(), request.getMessage());
        orderRepository.save(order);

        return new OrderResponse(
                order.getId(),
                option.getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }

    public void processOrderAndSendMessage(SendMessageRequest sendMessageRequest) {
        String bearerToken = sendMessageRequest.getBearerToken();
        OrderRequest orderRequest = sendMessageRequest.getOrderRequest();

        logger.info("Received sendMessageToMe request with Authorization: {}", bearerToken);
        logger.info("OrderRequest: {}", orderRequest);

        String accessToken = sendMessageRequest.getAccessToken();
        OrderResponse orderResponse = createOrder(orderRequest, accessToken);
        kakaoMessageService.sendMessage(orderResponse, accessToken);
    }
}