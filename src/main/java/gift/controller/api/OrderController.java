package gift.controller.api;

import gift.client.KakaoApiClient;
import gift.client.requestBody.KakaoMessageTemplateBody;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.exception.WishNotFoundException;
import gift.interceptor.MemberId;
import gift.repository.KakaoAccessTokenRepository;
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
    private final KakaoApiClient kakaoApiClient;
    private final KakaoAccessTokenRepository kakaoAccessTokenRepository;

    public OrderController(OptionService optionService, WishService wishService, OrderService orderService, KakaoApiClient kakaoApiClient, KakaoAccessTokenRepository kakaoAccessTokenRepository) {
        this.optionService = optionService;
        this.wishService = wishService;
        this.orderService = orderService;
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoAccessTokenRepository = kakaoAccessTokenRepository;
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

        String accessToken = kakaoAccessTokenRepository.getAccessToken(memberId);
        kakaoApiClient.sendMessageToMe(accessToken, new KakaoMessageTemplateBody(orderRequest.message()));

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
