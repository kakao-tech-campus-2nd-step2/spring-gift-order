package gift.product.controller;

import gift.product.dto.CategoryRequestDTO;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "API 컨트롤러")
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public ApiCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "카테고리 등록",
        description = "상품의 분류를 정하는 카테고리를 등록합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = {
                @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CategoryRequestDTO.class)
                )
            }
        )
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "카테고리 등록 성공",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Category.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "등록을 시도한 카테고리의 이름 누락"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "등록을 시도한 카테고리 이름이 기등록된 카테고리의 이름과 중복"
            )
        }
    )
    @PostMapping
    public ResponseEntity<Category> registerCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        System.out.println("[CategoryController] registerCategory()");
        Category category = categoryService.registerCategory(categoryRequestDTO.convertToDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        System.out.println("[CategoryController] updateCategory()");
        categoryService.updateCategory(new Category(id, category.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).body("Category registered successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        System.out.println("[CategoryController] deleteCategory()");
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Category deleted successfully");
    }

}
