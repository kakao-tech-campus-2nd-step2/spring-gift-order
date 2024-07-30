package gift.controller;

import gift.dto.SendMessageRequest;
import gift.value.AuthorizationHeader;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("Authorization") String authorizationHeader, @RequestBody OrderRequest orderRequest) {
        AuthorizationHeader authHeader;
        try {
            authHeader = new AuthorizationHeader(authorizationHeader);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // SendMessageRequest 생성
        SendMessageRequest sendMessageRequest = new SendMessageRequest(authorizationHeader, orderRequest);

        // 주문 생성 및 메시지 전송 처리
        orderService.processOrderAndSendMessage(sendMessageRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}