package gift.domain.product.controller;

import gift.domain.product.dto.CategoryResponse;
import gift.domain.product.service.CategoryManager;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryManager categoryManager;

    public CategoryRestController(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> readAll() {
        List<CategoryResponse> categories = categoryManager.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
