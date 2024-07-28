package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "상품 관리 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "상품 목록 조회", description = "모든 상품을 페이지 단위로 조회합니다.")
    @GetMapping
    public String getAllProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy,
                                 @RequestParam(defaultValue = "asc") String direction,
                                 Model model, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<ProductResponse> productPage = productService.findAll(PageRequest.of(page, size, sort));
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "index";
    }

    @Operation(summary = "상품 상세 조회", description = "ID를 이용하여 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{id}/edit")
    public String getProduct(@PathVariable long id, Model model, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        ProductResponse product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("productRequest", new ProductRequest(product.getName(), product.getPrice(), product.getImageUrl(), product.getCategoryId()));
        model.addAttribute("categories", categoryService.findAll());
        return "editForm";
    }

    @Operation(summary = "상품 추가 폼 조회", description = "새로운 상품 추가 폼을 조회합니다.")
    @GetMapping("/new")
    public String addProductForm(Model model, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        model.addAttribute("productRequest", new ProductRequest("", 0, "", 1L));
        model.addAttribute("categories", categoryService.findAll());
        return "addForm";
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping
    public String addProduct(@Valid @ModelAttribute ProductRequest productRequest, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "addForm";
        }
        try {
            productService.save(productRequest);
        } catch (IllegalArgumentException e) {
            bindingResult.addError(new FieldError("productRequest", "name", e.getMessage()));
            model.addAttribute("categories", categoryService.findAll());
            return "addForm";
        }
        return "redirect:/api/products";
    }

    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductRequest productRequest, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", new ProductResponse(id, productRequest.name(), productRequest.price(), productRequest.imageUrl(), null, null));
            model.addAttribute("categories", categoryService.findAll());
            return "editForm";
        }
        try {
            productService.update(id, productRequest);
        } catch (IllegalArgumentException e) {
            bindingResult.addError(new FieldError("productRequest", "name", e.getMessage()));
            model.addAttribute("product", new ProductResponse(id, productRequest.name(), productRequest.price(), productRequest.imageUrl(), null, null));
            model.addAttribute("categories", categoryService.findAll());
            return "editForm";
        }
        return "redirect:/api/products";
    }

    @Operation(summary = "상품 삭제", description = "ID를 이용하여 상품을 삭제합니다.")
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        if (!"ADMIN".equals(request.getAttribute("role"))) {
            return "redirect:/unauthorized";
        }

        productService.delete(id);
        return "redirect:/api/products";
    }

    @Operation(summary = "여러 상품 삭제", description = "여러 상품을 한번에 삭제합니다.")
    @PostMapping("/delete-batch")
    @ResponseBody
    public String deleteBatch(@RequestBody Map<String, List<Long>> request, HttpServletRequest httpRequest) {
        if (!"ADMIN".equals(httpRequest.getAttribute("role"))) {
            return "Unauthorized";
        }

        productService.deleteBatch(request.get("ids"));
        return "Success";
    }
}
