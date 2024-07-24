package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import org.springframework.http.HttpHeaders;
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

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader("Authorization") String jwtToken,
            @RequestHeader("Kakao-Authorization") String kakaoAccessToken,
            @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(jwtToken, kakaoAccessToken, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
