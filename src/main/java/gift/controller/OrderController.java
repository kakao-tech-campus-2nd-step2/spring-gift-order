package gift.controller;

import gift.dto.SendMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("Authorization") String accessToken, @RequestBody OrderRequest orderRequest) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = accessToken.substring(7); // "Bearer " 이후의 실제 토큰 값

        // SendMessageRequest 생성
        SendMessageRequest sendMessageRequest = new SendMessageRequest(accessToken, orderRequest);

        // 주문 생성 및 메시지 전송 처리
        orderService.processOrderAndSendMessage(sendMessageRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}