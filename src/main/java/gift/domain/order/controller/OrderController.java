package gift.domain.order.controller;

import gift.annotation.LoginMember;
import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> createOrder(@LoginMember Member member,
        @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(member, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }
}
