package gift.product.restapi;

import gift.core.domain.product.ProductOptionService;
import gift.product.restapi.dto.request.ProductOptionRegisterRequest;
import gift.product.restapi.dto.response.ProductOptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 옵션")
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회", description = "상품 옵션 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "상품 옵션 목록을 조회합니다.",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductOptionResponse.class)))
    )
    public List<ProductOptionResponse> getOptions(
            @PathVariable("productId") Long productId
    ) {
        return productOptionService
                .getOptionsFromProduct(productId)
                .stream()
                .map(ProductOptionResponse::from)
                .toList();
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 등록", description = "상품에 옵션을 등록합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "상품에 옵션을 등록합니다."
    )
    public void registerOption(
            @PathVariable Long productId,
            @RequestBody ProductOptionRegisterRequest request
    ) {
        productOptionService.registerOptionToProduct(productId, request.toDomain());
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "상품에서 옵션을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "상품에서 옵션을 삭제합니다."
    )
    public void deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {
        productOptionService.removeOptionFromProduct(productId, optionId);
    }
}
