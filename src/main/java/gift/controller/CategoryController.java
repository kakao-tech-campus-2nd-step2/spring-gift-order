package gift.controller;


import gift.dto.category.CategoryDTO;
import gift.entity.Category;
import gift.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/api/category")
    public Page<CategoryDTO> getCategory(@RequestParam(value = "page", defaultValue = "0") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 2, Sort.by(Sort.Direction.ASC, "id"));
        return categoryService.getCategory(pageable);
    }

    @PostMapping("/api/category")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveCategory(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return "카테고리 저장";
    }

    @PutMapping("/api/category")
    public String updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return "카테고리 수정";
    }

    @DeleteMapping("/api/category/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
        return "정상적으로 삭제됨";
    }

}
