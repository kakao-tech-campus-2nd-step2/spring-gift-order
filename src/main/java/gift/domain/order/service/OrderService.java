package gift.domain.order.service;

import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Order;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.User;
import gift.exception.ExternalApiException;
import gift.exception.InvalidOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemService orderItemService;
    private final KakaoTalkMessageService kakaoTalkMessageService;

    public OrderService(
        OrderJpaRepository orderJpaRepository,
        OrderItemService orderItemService,
        KakaoTalkMessageService kakaoTalkMessageService
    ) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemService = orderItemService;
        this.kakaoTalkMessageService = kakaoTalkMessageService;
    }

    @Transactional
    public OrderResponse createAndSendMessage(OrderRequest orderRequest, User user) {
        Order order = orderRequest.toOrder(user);

        orderItemService.create(user, order, orderRequest.orderItems());
        Order savedOrder = orderJpaRepository.save(order);

        if (user.getAuthProvider() != AuthProvider.KAKAO) {
            throw new InvalidOrderException("error.invalid.userinfo.provider");
        }

        if (kakaoTalkMessageService.sendMessageToMe(user, OrderResponse.from(savedOrder)) != 0) {
            throw new ExternalApiException("error.kakao.talk.message.response");
        };
        return OrderResponse.from(savedOrder);
    }
}
