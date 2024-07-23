package gift.order.controller;

import gift.common.auth.LoginMember;
import gift.common.util.CommonResponse;
import gift.member.model.Member;
import gift.option.domain.OrderResponse;
import gift.order.dto.OrderRequest;
import gift.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<?> requestOrder(@Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.requestOrder(orderRequest);
        CommonResponse<OrderResponse> response = new CommonResponse<>(orderResponse, "주문이 완료되었습니다.", true);

        return ResponseEntity.ok(response);
    }
}
