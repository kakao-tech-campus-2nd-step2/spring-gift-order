package gift.controller;

import gift.DTO.ProductOptionDTO;
import gift.mapper.ProductOptionMapper;
import gift.service.ProductOptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductOptionController {

    @Autowired
    private ProductOptionMapper productOptionMapper;

    @Autowired
    private ProductOptionService productOptionService;

    @GetMapping("/{id}/options")
    public List<ProductOptionDTO> getProductOption(@PathVariable Long id) {
        return productOptionService.getProductOptions(id);
    }

    @PostMapping("/{id}/options")
    public ProductOptionDTO createProductOption(@PathVariable Long id,
                                                @Valid @RequestBody ProductOptionDTO productOptionDTO) {
        return productOptionService.addProductOption(id, productOptionDTO);
    }

    @DeleteMapping("/{id}/options/{optionId}")
    public void deleteProductOption(@PathVariable Long id, @PathVariable Long optionId) {
        productOptionService.deleteProductOption(optionId);
    }
}
