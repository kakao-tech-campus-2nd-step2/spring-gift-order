package gift.service;

import static gift.controller.order.OrderMapper.toOrder;
import static gift.controller.order.OrderMapper.toOrderResponse;
import static gift.util.KakaoUtil.SendKakaoMessageToMe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.auth.KakaoToken;
import gift.controller.order.OrderRequest;
import gift.controller.order.OrderResponse;
import gift.domain.Order;
import gift.repository.OrderRepository;
import gift.util.KakaoUtil;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WishService wishService;
    private final OptionService optionService;

    public OrderService(OrderRepository orderRepository, WishService wishService,
        OptionService optionService) {
        this.orderRepository = orderRepository;
        this.wishService = wishService;
        this.optionService = optionService;
    }

    @Transactional
    public OrderResponse save(UUID memberId, OrderRequest orderRequest, KakaoToken token) {
        optionService.subtract(orderRequest.optionId(), orderRequest.quantity());
        wishService.delete(memberId,
            optionService.getOptionResponseById(orderRequest.optionId()).productId());
        Order target = orderRepository.save(
            toOrder(optionService.findById(orderRequest.optionId()), LocalDateTime.now(),
                orderRequest.message()));
        SendKakaoMessageToMe(orderRequest, token);
        return toOrderResponse(target.getId(), target.getOption().getId(), orderRequest.quantity(),
            target.getOrderDateTime(), target.getMessage());
    }
}