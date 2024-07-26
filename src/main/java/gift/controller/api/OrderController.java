package gift.controller.api;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.exception.WishNotFoundException;
import gift.interceptor.MemberId;
import gift.service.KakaoApiService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class OrderController {

    private final OptionService optionService;
    private final WishService wishService;
    private final OrderService orderService;
    private final KakaoApiService kakaoApiService;

    public OrderController(OptionService optionService, WishService wishService, OrderService orderService, KakaoApiService kakaoApiService) {
        this.optionService = optionService;
        this.wishService = wishService;
        this.orderService = orderService;
        this.kakaoApiService = kakaoApiService;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> order(@MemberId Long memberId, @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.saveOrder(orderRequest);

        optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());

        Long productId = optionService.getProductIdByOptionId(orderRequest);
        try {
            wishService.findAndDeleteProductInWish(memberId, productId);
        } catch (WishNotFoundException e) {
            Logger.getLogger(OrderController.class.getName()).log(Level.INFO, "위시리스트에 없는 상품입니다");
        }

        kakaoApiService.sendMessageToMe(memberId, orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
