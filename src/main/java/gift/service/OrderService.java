package gift.service;

import gift.domain.Order;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class OrderService {

    private final OrderSpringDataJpaRepository orderRepository;
    private final OptionService optionService;
    private final WishlistSpringDataJpaRepository wishlistRepository;
    private final KakaoMessageService kakaoMessageService;

    @Autowired
    public OrderService(OrderSpringDataJpaRepository orderRepository, OptionService optionService, WishlistSpringDataJpaRepository wishlistRepository, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistRepository = wishlistRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public OrderResponse createOrder(String token, OrderRequest orderRequest) {
        Long optionId = orderRequest.getOptionId();
        Integer quantity = orderRequest.getQuantity();
        Long productId = optionService.getProductIdByOptionId(optionId);

        optionService.subtractOptionQuantity(productId, optionId, quantity);

        wishlistRepository.deleteByMemberIdAndProductId(orderRequest.getReceiveMemberId(), productId);

        Order order = new Order(optionId, quantity, LocalDateTime.now(), orderRequest.getMessage(),orderRequest.getReceiveMemberId());

        orderRepository.save(order);

        kakaoMessageService.sendOrderMessage(token, order);

        return OrderResponse.fromOrder(order);
    }
}
