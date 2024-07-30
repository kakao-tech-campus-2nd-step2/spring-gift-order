package gift.controller;

import gift.dto.PaginationInfo;
import gift.dto.ProductDto;
import gift.dto.ProductResponse;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import gift.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view")
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;
    private final WishlistService wishlistService;

    public HomeController(ProductService productService, CategoryService categoryService, OptionService optionService, WishlistService wishlistService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
        this.wishlistService= wishlistService;
    }

    @GetMapping("/home")
    public String showHomeForm() {
        return "home";
    }


    @GetMapping("/products")
    public String showProductsPage() {
        return "products";
    }

    @GetMapping("/products/data")
    @ResponseBody
    public ProductResponse getProducts(Pageable pageable) {
        Page<Product> productPage = productService.getProducts(pageable);

        List<ProductDto> productDtoList = productPage.getContent().stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(productPage.getNumber());
        paginationInfo.setTotalPages(productPage.getTotalPages());
        paginationInfo.setHasNext(productPage.hasNext());
        paginationInfo.setHasPrevious(productPage.hasPrevious());

        ProductResponse response = new ProductResponse();
        response.setContent(productDtoList);
        response.setPagination(paginationInfo);

        return response;
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/view/products";
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("options", optionService.getAllOptions());

        model.addAttribute("product", new ProductDto(product));
        return "edit-product";
    }


    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/view/products";
    }

    @GetMapping("/productlist")
    public String showProductListsPage() {
        return "user-products";
    }


    @GetMapping("/wishlist/data")
    public ResponseEntity<ProductResponse> getWishlistItems(
            @RequestParam("email") String email,
            Pageable pageable) {
        Page<Product> productPage = wishlistService.getWishlistByEmail(email, pageable);
        List<ProductDto> productDtoList = productPage.getContent().stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPage(productPage.getNumber() + 1);
        paginationInfo.setTotalPages(productPage.getTotalPages());
        paginationInfo.setHasNext(productPage.hasNext());
        paginationInfo.setHasPrevious(productPage.hasPrevious());

        ProductResponse response = new ProductResponse();
        response.setContent(productDtoList);
        response.setPagination(paginationInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist")
    public String wishlistForm() {
        return "wishlist";
    }


}
