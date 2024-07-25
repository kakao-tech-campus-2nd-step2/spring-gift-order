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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "상품 옵션 관련 서비스")
@RestController
@RequestMapping("/api/products/{productId}/options")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @Autowired
    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @Operation(summary = "해당 상품 옵션 리스트 조회")
    @GetMapping
    public PageResult<optionSimple> getProductOptionList(@PathVariable long productId,
        @Valid getList req) {
        return PageMapper.toPageResult(productOptionService.getProductOptionList(productId, req));
    }

    @Operation(summary = "해당 상품의 어떤 옵션을 조회")
    @GetMapping("/{id}")
    public SingleResult<optionDetail> getProductOption(@PathVariable long productId,
        @PathVariable long id) {
        return new SingleResult<>(productOptionService.getProductOption(productId, id));
    }

    @Operation(summary = "상품 옵션 생성")
    @PostMapping
    public SingleResult<Long> createProductOption(@PathVariable long productId,
        @Valid @RequestBody CreateOption create) {
        return new SingleResult<>(productOptionService.createProductOption(productId, create));
    }

    @Operation(summary = "상품 옵션 수정")
    @PutMapping("/{id}")
    public SingleResult<Long> updateProductOption(@PathVariable long productId,
        @PathVariable long id, @Valid @RequestBody UpdateOption update) {
        return new SingleResult<>(productOptionService.updateProductOption(productId, id, update));
    }

    @Operation(summary = "상품 옵션 삭제", description = "상품은 최소 한개 이상의 옵션을 가져야 함")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteProductOption(@PathVariable long productId,
        @PathVariable long id) {
        return new SingleResult<>(productOptionService.deleteProductOption(productId, id));
    }
}
