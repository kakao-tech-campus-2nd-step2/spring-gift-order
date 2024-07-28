package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.BasicTokenService;
import gift.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final BasicTokenService basicTokenService;

    public OrderController(OrderService orderService, BasicTokenService basicTokenService) {
        this.orderService = orderService;
        this.basicTokenService = basicTokenService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrderWithMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody OrderRequestDto orderRequestDto
    ) throws IllegalAccessException {

        Long orderMemberId = basicTokenService.getUserIdByDecodeTokenValue(authorizationHeader);
        OrderResponseDto orderResponseDto = orderService.placeOrderWithMessage(orderRequestDto, orderMemberId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderResponseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(orderResponseDto);
    }
}
