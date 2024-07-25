package gift.product.controller.order;

import gift.product.dto.auth.OAuthLoginMember;
import gift.product.dto.order.OrderDto;
import gift.product.model.Order;
import gift.product.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final String KAKAO_SEND_MESSAGE_ME_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrderAll(HttpServletRequest request) {
        OAuthLoginMember loginMember = getOAuthLoginMember(request);
        List<Order> orderAll = orderService.getOrderAll(loginMember);

        return ResponseEntity.ok(orderAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        OAuthLoginMember loginMember = getOAuthLoginMember(request);
        Order order = orderService.getOrder(id, loginMember);

        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> doOrder(@RequestBody OrderDto orderDto,
        HttpServletRequest request) {
        OAuthLoginMember loginMember = getOAuthLoginMember(request);
        Order order = orderService.doOrder(orderDto, loginMember, KAKAO_SEND_MESSAGE_ME_URL);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        OAuthLoginMember loginMember = getOAuthLoginMember(request);
        orderService.deleteOrder(id, loginMember);

        return ResponseEntity.ok().build();
    }

    private OAuthLoginMember getOAuthLoginMember(HttpServletRequest request) {
        return new OAuthLoginMember((Long) request.getAttribute("id"),
            (String) request.getAttribute("oAuthAccessToken"));
    }
}
