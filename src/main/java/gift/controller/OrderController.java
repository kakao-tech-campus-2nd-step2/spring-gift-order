package gift.controller;

import gift.Login;
import gift.domain.Member;
import gift.dto.request.OrderRequest;
import gift.service.OrderService;
import jakarta.validation.Valid;
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
    public void order(@Login Member member, @Valid @RequestBody OrderRequest orderRequest) {
        orderService.order(member, orderRequest);
    }
}