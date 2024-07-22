package gift.controller;

import gift.dto.CategoryResponse;
import gift.entity.Category;
import gift.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
public class FrontCategoryController {

    private CategoryService categoryService;

    public FrontCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> response = categories.stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

}
