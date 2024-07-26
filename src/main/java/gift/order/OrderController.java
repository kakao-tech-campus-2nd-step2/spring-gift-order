package gift.order;

import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
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
    public CreateOrderResponseDTO createOrder(
        @RequestBody CreateOrderRequestDTO createOrderRequestDTO,
        HttpServletRequest httpServletRequest
    ) {
        return orderService.createOrder(
            createOrderRequestDTO,
            httpServletRequest.getHeader("Authorization")
        );
    }
}
