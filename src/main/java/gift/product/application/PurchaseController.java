package gift.product.application;

import gift.auth.interceptor.Authorized;
import gift.auth.resolver.LoginMember;
import gift.product.application.dto.request.ProductPurchaseRequest;
import gift.product.service.facade.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options/purchase")
public class PurchaseController {
    private final ProductFacade productFacade;

    public PurchaseController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping()
    @Authorized()
    @ResponseStatus(HttpStatus.OK)
    public void purchaseProduct(@PathVariable("productId") Long productId,
                                @LoginMember Long loginMember,
                                @RequestBody ProductPurchaseRequest productPurchaseRequest
    ) {
        var command = productPurchaseRequest.toCommand(productId, loginMember);

        productFacade.purchaseProduct(command);
    }

}
