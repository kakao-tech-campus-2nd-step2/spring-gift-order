package gift.controller.api;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.exception.WishNotFoundException;
import gift.interceptor.MemberId;
import gift.service.KakaoMessageService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoMessageService messageService;
    private final OrderService orderService;

    public OrderController(OptionService optionService, WishService wishService, KakaoMessageService messageService, OrderService orderService) {
        this.optionService = optionService;
        this.wishService = wishService;
        this.messageService = messageService;
        this.orderService = orderService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> order(@MemberId Long memberId, @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.saveOrder(orderRequest);
        //오더에 옵션 id, 주문량, 메세지 존재.
        optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());
        //상품 아이디 겟
        Long productId = optionService.getProductIdByOptionId(orderRequest);
        //위시에 상품 있으면
        try {
            wishService.deleteProductInWish(memberId, productId);
        } catch (WishNotFoundException e) {
            System.out.println("위시에 없음");//이거 분리시키자
        }
        //api 액세스 토큰 키:멤버id 벨류: 액세스토큰
        messageService.sendToMe(memberId, orderResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
