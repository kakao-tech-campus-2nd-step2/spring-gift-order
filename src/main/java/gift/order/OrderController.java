package gift.order;

import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateOrderResponseDTO createOrder(
        @RequestBody CreateOrderRequestDTO createOrderRequestDTO,
        @RequestHeader("Authorization") String accessToken
    ) {
        return orderService.createOrder(createOrderRequestDTO, accessToken);
    }
}
