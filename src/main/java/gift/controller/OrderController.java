package gift.controller;

import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.User.UserResponse;
import gift.domain.User;
import gift.security.AuthenticateMember;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Void> order(
            @RequestBody OrderRequest orderRequest,
            @AuthenticateMember UserResponse user
    ){
        orderService.order(orderRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
