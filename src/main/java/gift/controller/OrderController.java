package gift.controller;

import gift.classes.RequestState.OrderRequestStateDTO;
import gift.classes.RequestState.RequestStatus;
import gift.dto.OrderDto;
import gift.dto.RequestOrderDto;
import gift.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderRequestStateDTO> addOrder(@RequestBody RequestOrderDto requestOrderDto,
        @RequestHeader("Authorization") String token) {
        OrderDto orderDto = orderService.addOrder(requestOrderDto, token);
        return ResponseEntity.ok().body(new OrderRequestStateDTO(
            RequestStatus.success,
            null,
            orderDto
        ));
    }
}
