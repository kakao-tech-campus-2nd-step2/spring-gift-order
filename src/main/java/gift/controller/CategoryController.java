package gift.controller;

import gift.dto.CategoryResponseDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name="category",description = "카테고리 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회합니다.")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDto = categoryService.getAllCategoryResponseDto();
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{product_id}")
    @Operation(summary = "특정 상품 카테고리 조회", description = "해당 상품 id를 가지고 카테고리를 조회합니다.")
    public ResponseEntity<CategoryResponseDto> getCategoryDtoByProductId(@RequestParam("product_id") Long productId) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryDtoByProductId(productId);
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }
}
