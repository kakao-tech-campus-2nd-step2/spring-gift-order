package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.exception.ErrorCode;
import gift.exception.customException.AccessDeniedException;
import gift.model.order.OrderDTO;
import gift.model.order.OrderForm;
import gift.service.OrderService;
import gift.service.UserService;
import java.io.UnsupportedEncodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> handleOrder(@RequestBody OrderForm form,
        @RequestAttribute("userId") Long userId)
        throws UnsupportedEncodingException, JsonProcessingException {
        if (!userService.isKakaoUser(userId)) {
            throw new AccessDeniedException(ErrorCode.KAKAO_ACCESS_DENIED);
        }
        return ResponseEntity.ok(orderService.executeOrder(userId, new OrderDTO(form)));
    }
}
