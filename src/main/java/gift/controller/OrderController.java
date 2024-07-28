package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request) {
        Map<String, Object> kakaoUserInfo = (Map<String, Object>) request.getAttribute("kakaoUserInfo");
        OrderResponse orderResponse = orderService.createOrder(orderRequest, kakaoUserInfo);
        return ResponseEntity.status(201).body(orderResponse);
    }
}
