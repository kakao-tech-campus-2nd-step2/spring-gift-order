package gift.controller;

import gift.dto.KakaoTokenDto;
import gift.dto.ProductDto;

import gift.dto.OptionDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/products")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private OptionService optionService;
    private WishlistService wishlistService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, OptionService optionService, WishlistService wishlistService,KakaoService kakaoService, KakaoTokenService kakaoTokenService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
    }



    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", new ProductDto());

            model.addAttribute("categories", categoryService.getAllCategories());
            return "add-product";
        }
        productService.addProduct(productDto);
        return "redirect:/view/products";
    }



    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductDto productDto, BindingResult result, Model model) {

        Product product = productService.getProductById(productDto.getId());
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("options", optionService.getAllOptions());
            model.addAttribute("product", new ProductDto(product));

            return "edit-product";
        }
        if (productDto.getName().contains("카카오")) {
            result.rejectValue("name", "error.product", "상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의하십시오.");
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "edit-product";
        }
        productService.updateProduct(id, productDto);
        return "redirect:/view/products";
    }

    @Transactional
    @PostMapping("/order/{productId}")
    public ResponseEntity<Void> orderItem( @RequestParam("email") String email, @RequestParam("optionId") Long optionId, @RequestParam("quantity") int quantity, @PathVariable Long productId, @RequestParam("message") String message) {
        optionService.subtractOptionQuantity(optionId, quantity);
        wishlistService.deleteWishlistItem(email, productId);
        System.out.println(message);
        KakaoTokenDto tokenDto = kakaoTokenService.getTokenByEmail(email);
        String accessToken = tokenDto.getAccessToken();
        kakaoService.sendKakaoMessage(accessToken, message);
        return ResponseEntity.ok().build();
    }

}