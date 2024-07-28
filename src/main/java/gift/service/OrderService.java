package gift.service;

import gift.authentication.token.JwtResolver;
import gift.authentication.token.Token;
import gift.config.KakaoProperties;
import gift.web.client.KakaoClient;
import gift.web.client.dto.KakaoCommerce;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.response.order.OrderResponse;
import gift.web.dto.response.product.ReadProductResponse;
import gift.web.dto.response.productoption.SubtractProductOptionQuantityResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final KakaoClient kakaoClient;
    private final JwtResolver jwtResolver;
    private final ProductOptionService productOptionService;
    private final ProductService productService;
    private final KakaoProperties kakaoProperties;

    public OrderService(KakaoClient kakaoClient, JwtResolver jwtResolver, ProductOptionService productOptionService,
        ProductService productService, KakaoProperties kakaoProperties) {
        this.kakaoClient = kakaoClient;
        this.jwtResolver = jwtResolver;
        this.productOptionService = productOptionService;
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
    }

    @Transactional
    public OrderResponse createOrder(String accessToken, Long productId, CreateOrderRequest request) {
        //상품 옵션 수량 차감
        SubtractProductOptionQuantityResponse subtractOptionStockResponse = productOptionService.subtractOptionStock(request);

        ReadProductResponse product = productService.readProductById(productId);
        KakaoCommerce kakaoCommerce = KakaoCommerce.of(product, request.getMessage());

        sendOrderMessageIfSocialMember(accessToken, kakaoCommerce);
        return new OrderResponse(
            productId,
            request.getOptionId(),
            subtractOptionStockResponse.getStock(),
            request.getQuantity(),
            product.getName(),
            request.getMessage());
    }

    /**
     * 소셜 로그인을 통해 주문한 경우 카카오톡 메시지를 전송합니다
     * @param accessToken Bearer Token
     * @param kakaoCommerce 카카오 상거래 메시지
     */
    private void sendOrderMessageIfSocialMember(String accessToken, KakaoCommerce kakaoCommerce) {
        jwtResolver.resolveSocialToken(Token.fromBearer(accessToken))
            .ifPresent(socialToken -> {
                String json = kakaoCommerce.toJson();
                kakaoClient.sendMessage(
                    kakaoProperties.getMessageUrlAsUri(),
                    getBearerToken(socialToken),
                    json);
            });
    }

    private String getBearerToken(String token) {
        return "Bearer " + token;
    }
}
