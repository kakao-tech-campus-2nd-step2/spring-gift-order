package gift.controller;

import gift.domain.Order;
import gift.dto.CreateOrderDto;
import gift.service.OrderService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping()
    public Order createOrder(@RequestBody CreateOrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }
}
