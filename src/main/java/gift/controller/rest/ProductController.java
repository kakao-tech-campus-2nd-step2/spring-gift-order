package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.ProductDTO;
import gift.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // http://localhost:8080/api/products?page=1&size=3
    @GetMapping()
    public List<Product> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        return products.getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping()
    public ResponseEntity<Product> postProduct(@RequestBody @Valid ProductDTO form, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.save(form, email);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> putProduct(@RequestBody @Valid ProductDTO form,
                                              @PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.update(id, form, email);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<MessageResponseDTO> deleteProduct(@PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.delete(id, email);
        return ResponseEntity
                .ok()
                .body(new MessageResponseDTO("Product deleted successfully"));
    }

    // option
    @GetMapping("/{id}/options")
    public ResponseEntity<List<Option>> getProductOptions(@PathVariable("id") Long id) {
        List<Option> result = productService.getOptions(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> postProductOptions(@PathVariable("product_id") Long product_id,
                                                                 @PathVariable("option_id") Long option_id,
                                                                 HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.addProductOption(product_id, option_id, email);
        return ResponseEntity
                .ok()
                .body(new MessageResponseDTO("Option added successfully"));
    }

    @DeleteMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> deleteProductOptions(@PathVariable("product_id") Long product_id,
                                                                   @PathVariable("option_id") Long option_id,
                                                                   HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.deleteProductOption(product_id, option_id, email);
        return ResponseEntity
                .ok()
                .body(new MessageResponseDTO("Option deleted successfully"));
    }
}
