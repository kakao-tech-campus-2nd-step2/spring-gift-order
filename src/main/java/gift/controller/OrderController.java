package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.KakaoTokenService;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final KakaoTokenService kakaoTokenService;

    public OrderController(OrderService orderService, KakaoTokenService kakaoTokenService) {
        this.orderService = orderService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        // 주문 처리 로직
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        kakaoTokenService.sendKakaoMessage(orderResponse);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
