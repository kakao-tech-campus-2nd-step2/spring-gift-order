package gift.order.kakao.service;

import gift.option.service.OptionService;
import gift.order.kakao.dto.OrderRequestDto;
import gift.product.service.ProductService;
import gift.wishlist.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final WishListService wishListService;
    private final OptionService optionService;

    @Autowired
    public OrderService(ProductService productService, WishListService wishListService,
        OptionService optionService) {
        this.productService = productService;
        this.wishListService = wishListService;
        this.optionService = optionService;
    }

    // 주문용 service
    @Transactional
    public void order(OrderRequestDto orderRequestDto, long userId) {
        // 개수 차감
        var actualOption = optionService.selectOption(orderRequestDto.optionId());
        productService.subtractOption(actualOption.productId(), userId, orderRequestDto.quantity());

        // 위시리스트 제거
        var productResponseDto = productService.selectProduct(actualOption.productId());
        wishListService.orderWishProduct(productResponseDto, userId);
    }
}
