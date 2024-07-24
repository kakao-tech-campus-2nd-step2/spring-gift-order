package gift.controller;

import gift.config.LoginMember;
import gift.domain.member.Member;
import gift.dto.OrderDto;
import gift.dto.request.OrderCreateRequest;
import gift.dto.response.OrderResponse;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> order(@LoginMember Member member,
                                               @RequestBody @Valid OrderCreateRequest request) {
        OrderDto dto = OrderDto.of(member, request);
        OrderDto orderDto = orderService.processOrder(dto);
        OrderResponse response = orderDto.toResponseDto();

        return ResponseEntity.status(CREATED)
                .body(response);
    }

}
