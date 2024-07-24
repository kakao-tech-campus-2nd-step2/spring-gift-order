package gift.order.restapi;

import gift.advice.GatewayToken;
import gift.advice.LoggedInUser;
import gift.core.domain.order.Order;
import gift.core.domain.order.OrderService;
import gift.order.restapi.dto.OrderRequest;
import gift.order.restapi.dto.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public OrderResponse orderProduct(
            @LoggedInUser Long userId,
            @GatewayToken String gatewayAccessToken,
            @RequestBody OrderRequest request
    ) {
        Order order = orderService.orderProduct(request.toOrder(userId), gatewayAccessToken);
        return OrderResponse.of(order);
    }
}
