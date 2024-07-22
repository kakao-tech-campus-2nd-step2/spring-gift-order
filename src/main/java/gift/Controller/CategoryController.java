package gift.Controller;


import gift.Model.Category;
import gift.DTO.RequestCategoryDTO;
import gift.DTO.ResponseCategoryDTO;
import gift.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<String> addCategory(@RequestBody RequestCategoryDTO requestCategoryDTO){
        Category category = categoryService.addCategory(requestCategoryDTO);
        return ResponseEntity.created(URI.create("api/categories/"+ category.getId())).body("카테고리가 정상적으로 추가되었습니다");
    }

    @GetMapping
    public ResponseEntity<List<ResponseCategoryDTO>> getCategories (){
        List<ResponseCategoryDTO> categoryList = categoryService.getCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity <String> editCategory(@RequestBody RequestCategoryDTO requestCategoryDTO){
        categoryService.editCategory(requestCategoryDTO);
        return ResponseEntity.ok("카테고리를 정상적으로 수정하였습니다");
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<String> deleteCategory(@PathVariable ("category-id") Long categoryId ){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리가 정상적으로 삭제되었습니다");
    }
}
