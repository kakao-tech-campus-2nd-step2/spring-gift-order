package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
@Tag(name = "Category Page Controller", description = "Category page operations")
public class CategoryPageController {

    private final CategoryService categoryService;

    public CategoryPageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "View category page")
    @GetMapping
    public String viewCategoryPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category";
    }

    @Operation(summary = "Form to create new category")
    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        CategoryDTO categoryDTO = new CategoryDTO();
        model.addAttribute("category", categoryDTO);
        return "addCategory";
    }

    @Operation(summary = "Create a new category")
    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addCategory";
        }
        categoryService.create(categoryDTO);
        return "redirect:/categories";
    }

    @Operation(summary = "Form to update an existing category")
    @GetMapping("/update/{id}")
    public String updateCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getById(id);
        model.addAttribute("category", categoryDTO);
        return "editCategory";
    }

    @Operation(summary = "Update an existing category")
    @PutMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editCategory";
        }
        categoryService.update(id, categoryDTO);
        return "redirect:/categories";
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/categories";
    }
}
