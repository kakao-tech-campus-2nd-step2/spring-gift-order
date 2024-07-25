package gift.controller;

import gift.model.OrderDTO;
import gift.service.OrderService;
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

    @PostMapping
    public ResponseEntity<?> sendOrderMessage(@RequestBody OrderDTO orderDTO) {
        OrderDTO response = orderService.createOrder(orderDTO);
        orderService.sendOrderMessage(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
