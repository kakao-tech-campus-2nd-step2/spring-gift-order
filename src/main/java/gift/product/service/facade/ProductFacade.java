package gift.product.service.facade;

import gift.auth.service.KakaoOAuthService;
import gift.common.annotation.Facade;
import gift.product.service.KakaoMessageService;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.product.service.command.BuyProductMessageCommand;
import gift.product.service.command.ProductCommand;
import gift.product.service.command.ProductOptionCommand;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Facade
public class ProductFacade {
    private static final Logger log = LoggerFactory.getLogger(ProductFacade.class);
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    private final KakaoOAuthService kakaoOAuthService;
    private final KakaoMessageService kakaoMessageService;

    public ProductFacade(ProductService productService, ProductOptionService productOptionService,
                         KakaoOAuthService kakaoOAuthService, KakaoMessageService kakaoMessageService) {
        this.productService = productService;
        this.productOptionService = productOptionService;
        this.kakaoOAuthService = kakaoOAuthService;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional
    public Long saveProduct(ProductCommand productCommand, List<ProductOptionCommand> productOptionCommands) {
        var productId = productService.saveProduct(productCommand);
        productOptionService.createProductOptions(productId, productOptionCommands);
        return productId;
    }

    public boolean purchaseProduct(Long productId, Long optionId, Integer amount) {
        productOptionService.buyProduct(productId, optionId, amount);
        var accessToken = kakaoOAuthService.getAccessToken("lisjb1998@naver.com");
        var productInfo = productService.getProductDetails(productId);
        var optionInfo = productOptionService.getProductOptionInfo(productId, optionId);

        var response = kakaoMessageService.sendBuyProductMessage(
                BuyProductMessageCommand.of(productInfo, optionInfo, accessToken));
        return response;
    }
}
