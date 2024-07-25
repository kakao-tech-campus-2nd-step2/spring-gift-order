package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.LoginMember;
import gift.model.Member;
import gift.model.Order;
import gift.model.Product;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final WishService wishService;
    private final OptionService optionService;

    public OrderController(OrderService orderService, WishService wishService, OptionService optionService) {
        this.orderService = orderService;
        this.wishService = wishService;
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> make(@RequestBody @Valid OrderRequest request, @LoginMember Member member) {
        // 옵션 개수 차감
        optionService.subtractQuantity(request);

        // 해당 상품이 wish list 에 있다면 제거
        wishService.deleteWish(
                optionService.getProductById(request).getId(),
                member
        );

        // 주문 메시지 보냄
        orderService.sendOrderMessage(request, member);

        // 주문
        Order order = orderService.make(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.toDto());
    }
}
