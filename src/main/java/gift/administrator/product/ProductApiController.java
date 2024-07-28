package gift.administrator.product;

import gift.util.PageUtil;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {
        size = PageUtil.validateSize(size);
        sortBy = PageUtil.validateSortBy(sortBy, Arrays.asList("id", "name"));
        Direction direction = PageUtil.validateDirection(sortDirection);
        Page<ProductDTO> productPage = productService.getAllProducts(page, size, sortBy,
            direction);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAddingProducts(productDTO);
        ProductDTO result = productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id,
        @Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAndId(productDTO.getName(), id);

        ProductDTO result = productService.updateProduct(productDTO, id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.getProductById(id);
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
