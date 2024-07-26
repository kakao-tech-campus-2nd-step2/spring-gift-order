package gift.api.order;

import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.global.resolver.LoginMember;
import java.net.URI;
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
    public ResponseEntity<OrderResponse> order(@LoginMember Long memberId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.order(memberId, orderRequest);
        orderService.sendMessage(memberId, orderRequest);
        return ResponseEntity.created(URI.create("/api/orders")).body(orderResponse);
    }
}
