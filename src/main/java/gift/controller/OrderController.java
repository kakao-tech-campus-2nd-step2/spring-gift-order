package gift.controller;

import gift.config.LoginUser;
import gift.dto.OrderDetailResponse;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.User;
import gift.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<OrderResponse> createOrder(@LoginUser User user, @Valid @RequestBody
    OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.createOrder(user.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderDetailResponse>> getAllOrders(@LoginUser User user) {
        return ResponseEntity.ok().body(orderService.getAllOrders(user.getId()));
    }

}
