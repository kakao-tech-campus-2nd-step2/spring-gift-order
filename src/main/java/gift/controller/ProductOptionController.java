package gift.controller;

import gift.domain.ProductOption.CreateOption;
import gift.domain.ProductOption.UpdateOption;
import gift.domain.ProductOption.getList;
import gift.domain.ProductOption.optionDetail;
import gift.domain.ProductOption.optionSimple;
import gift.service.ProductOptionService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @Autowired
    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @GetMapping
    public PageResult<optionSimple> getProductOptionList(@PathVariable long productId,
        @Valid getList req) {
        return PageMapper.toPageResult(productOptionService.getProductOptionList(productId, req));
    }

    @GetMapping("/{id}")
    public SingleResult<optionDetail> getProductOption(@PathVariable long productId,
        @PathVariable long id) {
        return new SingleResult<>(productOptionService.getProductOption(productId, id));
    }

    @PostMapping
    public SingleResult<Long> createProductOption(@PathVariable long productId,
        @Valid @RequestBody CreateOption create) {
        return new SingleResult<>(productOptionService.createProductOption(productId, create));
    }

    @PutMapping("/{id}")
    public SingleResult<Long> updateProductOption(@PathVariable long productId,
        @PathVariable long id, @Valid @RequestBody UpdateOption update) {
        return new SingleResult<>(productOptionService.updateProductOption(productId, id, update));
    }

    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteProductOption(@PathVariable long productId,
        @PathVariable long id) {
        return new SingleResult<>(productOptionService.deleteProductOption(productId, id));
    }
}
