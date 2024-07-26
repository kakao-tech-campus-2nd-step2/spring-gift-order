package gift.domain.order.controller;

import gift.config.LoginUser;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import gift.domain.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest orderRequest, @LoginUser User user) {
        OrderResponse orderResponse = orderService.create(orderRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
