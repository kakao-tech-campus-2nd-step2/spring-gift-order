package gift.doamin.order.controller;

import gift.doamin.order.dto.OrderRequest;
import gift.doamin.order.dto.OrderResponse;
import gift.doamin.order.service.OrderService;
import gift.doamin.user.entity.User;
import gift.global.LoginUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponse makeOrder(@Valid @RequestBody OrderRequest orderRequest,
        @LoginUser User user) {

        OrderResponse orderResponse = orderService.makeOrder(user, orderRequest);
        System.out.println("orderResponse = " + orderResponse);
        return orderResponse;
    }

}
