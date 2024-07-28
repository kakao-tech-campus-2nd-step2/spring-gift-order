package gift.controller;

import gift.domain.Member;
import gift.dto.OrderRequest;
import gift.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        try {
            orderService.placeOrder(orderRequest, member.getId());
            return ResponseEntity.ok("주문이 완료되었습니다!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("주문 실패: " + e.getMessage());
        }
    }
}
