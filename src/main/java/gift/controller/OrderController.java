package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.dto.OrderAddRequestDto;
import gift.domain.model.dto.OrderResponseDto;
import gift.domain.model.entity.User;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(@LoginUser User user,
        @Valid @RequestBody OrderAddRequestDto orderAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.addOrder(user, orderAddRequestDto));
    }
}
