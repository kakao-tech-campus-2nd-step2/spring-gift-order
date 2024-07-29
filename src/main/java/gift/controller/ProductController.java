package gift.controller;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductDto> getProducts(Pageable pageable) {
        return productService.getProducts(pageable).map(product -> new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                product.getCategory().getId(),
                product.getOptions().stream()
                        .map(option -> new OptionDto(option.getId(), option.getName(), option.getQuantity()))
                        .collect(Collectors.toList())
        ));
    }

    @GetMapping("/all")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") Long id) {
        return productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }

    @PostMapping("/add")
    public Long addProduct(@RequestBody ProductDto productDto) {
        return productService.save(productDto);
    }
    
    @PutMapping("/update/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        productService.update(id, productDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/{productId}/option")
    public List<OptionDto> getProductOptions(@PathVariable Long productId) {
        return productService.getProductOptions(productId);
    }

    @PostMapping("/{productId}/addOption")
    public void addOptionToProduct(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
        productService.addOptionToProduct(productId, optionDto);
    }

    @PostMapping("/{productId}/option/{optionId}/subtract")
    public void subtractOptionQuantity(@PathVariable Long productId, @PathVariable Long optionId, @RequestParam int quantity) {
        productService.subtractOptionQuantity(productId, optionId, quantity);
    }
}