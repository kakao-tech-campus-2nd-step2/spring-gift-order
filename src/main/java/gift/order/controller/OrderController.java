package gift.order.controller;

import gift.order.domain.OrderRequest;
import gift.order.domain.OrderResponse;
import gift.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> getOrder(@RequestBody OrderRequest orderRequest){
        // 1. 주문 저장
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        // 2. 옵션 수량 차감, wishlist에서 제거

        // 3. 카카오톡 메시지 api 전송

        // 4. response 반환

        return ResponseEntity.ok(orderResponse);
    }
}
