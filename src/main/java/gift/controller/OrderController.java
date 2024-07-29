package gift.controller;

import gift.authentication.LoginMember;
import gift.authentication.UserDetails;
import gift.dto.ApiResponse;
import gift.dto.OrderRequestDto;
import gift.service.OrderService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse> makeOrder(@LoginMember UserDetails userDetails, @Valid @RequestBody OrderRequestDto request) {
        orderService.handleOrder(userDetails.id(), request);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.CREATED, "주문에 성공하였습니다"));
    }
}
