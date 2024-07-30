package gift.controller;

import gift.domain.member.MemberResponse;
import gift.domain.order.OrderRequest;
import gift.domain.order.OrderResponse;
import gift.service.OrderService;
import gift.util.AuthAspect;
import gift.util.AuthenticatedMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    @AuthenticatedMember
    public ResponseEntity<OrderResponse> createOrder(HttpServletRequest httpServletRequest,
        @Valid @RequestBody OrderRequest createOrderRequest) {
        MemberResponse member = (MemberResponse) httpServletRequest.getAttribute(
            AuthAspect.ATTRIBUTE_NAME_AUTH_MEMBER);

        OrderResponse orderResponse = orderService.create(member, createOrderRequest);
        return ResponseEntity.created(URI.create("")).body(orderResponse);
    }
}
