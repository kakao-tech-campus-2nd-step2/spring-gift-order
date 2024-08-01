package gift.order.kakao.controller;

import gift.global.annotation.UserId;
import gift.order.kakao.dto.OrderRequestDto;
import gift.order.kakao.service.OrderService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class OrderApiController {

    private final OrderService orderService;

    @Autowired
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/order")
    public ResponseEntity<String> order(@RequestBody OrderRequestDto orderRequestDto,
        @UserId Long userId) {

        return ResponseEntity.ok("");
    }
}
