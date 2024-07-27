package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String kakaoToken = authorizationHeader.replace("Bearer ", "");
        OrderResponse orderResponse = orderService.createOrder(orderRequest, kakaoToken);
        return ResponseEntity.status(201).body(orderResponse);
    }
}
