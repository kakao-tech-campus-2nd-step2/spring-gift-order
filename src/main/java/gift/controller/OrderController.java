package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("Authorization") String accessToken, @RequestBody OrderRequest orderRequest) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = accessToken.substring(7); // "Bearer " 이후의 실제 토큰 값
        OrderResponse orderResponse = orderService.createOrder(orderRequest, token);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}