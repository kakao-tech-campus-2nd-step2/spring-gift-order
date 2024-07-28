package gift.controller;

import gift.domain.Member;
import gift.dto.OrderDTO;
import gift.dto.OrderRequest;
import gift.service.OrderService;
import gift.util.LoginMember;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<OrderDTO> createOrder(@LoginMember Member member, @RequestBody OrderRequest orderRequest) {
        OrderDTO orderDTO = orderService.createOrder(member.getId(), orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage());
        return ResponseEntity.status(201).body(orderDTO);
    }
}
