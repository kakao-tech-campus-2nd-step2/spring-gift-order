package gift.controller;

import gift.dto.OrderDTO;
import gift.model.User;
import gift.security.LoginMember;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, @LoginMember User user) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO, user);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/order")
    public String showOrderPage() {
        return "order";  // templates/order.html 템플릿을 반환
    }
}