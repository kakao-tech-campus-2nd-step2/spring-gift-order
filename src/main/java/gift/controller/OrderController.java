package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "카카오 주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 추가", description = "새로운 주문을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 추가 성공",
            content = {@Content(schema = @Schema(implementation = OrderResponse.class))}),
        @ApiResponse(responseCode = "400", description = "해당 옵션을 찾을 수 없음 또는 재고 부족")
    })
    public ResponseEntity<OrderResponse> addOrder(@RequestHeader("Authorization") String token,
        @RequestBody OrderRequest orderRequest) {

        OrderResponse response = orderService.addOrder(orderRequest, token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
