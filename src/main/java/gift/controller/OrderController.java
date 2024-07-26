package gift.controller;

import gift.domain.OrderDTO;
import gift.service.JwtUtil;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    private JwtUtil jwtUtil;

    @Autowired
    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token, @RequestBody OrderDTO order) {
        String parsedToken = token.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(parsedToken);
        String email = jwtUtil.extractEmail(parsedToken);

        orderService.createOrder(order, userId, email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
