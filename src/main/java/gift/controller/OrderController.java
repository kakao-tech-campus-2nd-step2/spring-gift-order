package gift.controller;

import gift.dto.OrderRequestDTO;
import gift.model.Order;
import gift.service.OrderService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            String email = getEmailFromToken(token);
            Order order = orderService.placeOrder(email, orderRequestDTO);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getEmailFromToken(String token) {
        return JwtUtil.generateToken(token.substring(7));
    }
}
