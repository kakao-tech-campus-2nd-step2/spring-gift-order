package gift.controller;

import gift.domain.Category;
import gift.domain.Category.CreateCategory;
import gift.domain.Category.DetailCategory;
import gift.domain.Category.SimpleCategory;
import gift.domain.Category.UpdateCategory;
import gift.service.CategoryService;
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

@Tag(name = "상품 카테고리관련 서비스")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록을 조회한다.")
    @GetMapping
    public PageResult<SimpleCategory> getCategoryList(@Valid Category.getList req) {
        return PageMapper.toPageResult(categoryService.getCategoryList(req));
    }

    @Operation(summary = "단일 카테고리를 조회한다.")
    @GetMapping("/{id}")
    public SingleResult<DetailCategory> getCategory(@PathVariable long id) {
        return new SingleResult(categoryService.getCategory(id));
    }

    @Operation(summary = "카테고리를 생성한다.")
    @PostMapping
    public SingleResult<Long> createCategory(@Valid @RequestBody CreateCategory create) {
        return new SingleResult(categoryService.createCategory(create));
    }

    @Operation(summary = "카테고리를 수정한다.")
    @PutMapping("/{id}")
    public SingleResult<Long> updateCategory(@PathVariable long id,
        @Valid @RequestBody UpdateCategory update) {
        return new SingleResult(categoryService.updateCategory(id, update));
    }

    @Operation(summary = "카테고리를 삭제한다.")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteCategory(@PathVariable long id) {
        return new SingleResult(categoryService.deleteCategory(id));
    }

}
