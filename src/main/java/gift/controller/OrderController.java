package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.JwtUtil;
import gift.service.OrderService;
import gift.vo.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = JwtUtil.getBearTokenAndMemberId(authorizationHeader);

        Order successOrder = orderService.createOrder(memberId, orderRequestDto);

        return new ResponseEntity<>(OrderResponseDto.toOrderResponseDto(successOrder), HttpStatus.CREATED);
    }

}
