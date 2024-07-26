package gift.controller;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import gift.service.KakaoMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("api/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("Authorization") String authorization, @RequestBody OrderRequest orderRequest) {
        String token = authorization.replace("Bearer ", "");
        OrderResponse orderResponse = orderService.createOrder(token, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}