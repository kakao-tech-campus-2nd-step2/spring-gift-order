package gift.Controller;

import gift.Model.request.OrderRequest;
import gift.Model.response.OrderResponse;
import gift.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> order(@RequestHeader("Authorization") String accessToken, @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.order(orderRequest);
        orderService.sendKakaoTalkMessage(accessToken, orderResponse);
        return ResponseEntity.ok(orderResponse);
    }
}
