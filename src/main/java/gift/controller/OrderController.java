package gift.controller;

import gift.anotation.LoginMember;
import gift.domain.Member;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<OrderResponseDTO> createOrder(@LoginMember Member member, @RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO orderResponse = orderService.createOrder(member, orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}