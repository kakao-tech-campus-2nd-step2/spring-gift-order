package gift.order.controller;

import gift.annotation.LoginUser;
import gift.order.dto.request.OrderRequest;
import gift.order.dto.response.OrderResponse;
import gift.order.service.OrderService;
import gift.user.entity.User;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@LoginUser User user,
        @RequestBody OrderRequest orderRequest) {
        var response = orderService.createOrder(user.getId(), orderRequest);
        URI location = UriComponentsBuilder.fromPath("/api/orders/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
