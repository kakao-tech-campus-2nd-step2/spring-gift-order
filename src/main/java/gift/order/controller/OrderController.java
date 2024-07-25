package gift.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.common.util.CommonResponse;
import gift.option.domain.OrderResponse;
import gift.order.dto.OrderRequest;
import gift.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<?> requestOrder(
            @Valid OrderRequest orderRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws JsonProcessingException {
        // Bearer 접두사를 제거하여 액세스 토큰만 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // 주문 요청 및 주문 내역 저장
        OrderResponse orderResponse = orderService.requestOrder(orderRequest);

        // 주문 내역 카카오톡 메시지를 통해 나에게 보내기
        orderService.sendKakaoMessage(orderResponse, accessToken);

        CommonResponse<OrderResponse> response = new CommonResponse<>(orderResponse, "주문이 완료되었습니다.", true);

        return ResponseEntity.ok(response);
    }
}
