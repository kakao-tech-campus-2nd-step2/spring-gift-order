package gift.product.controller;

import gift.product.dto.ProductDTO;
import gift.product.service.WishListService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class ApiWishListController {

    private final WishListService wishListService;

    @Autowired
    public ApiWishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping()
    public Page<ProductDTO> showProductList(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable
    ) {
        System.out.println("[ApiWishListController] showProductList()");

        return wishListService.getAllProducts(authorization, pageable);
    }

    @PostMapping()
    public ResponseEntity<String> registerWishProduct(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, Long> requestBody
    ) {
        System.out.println("[ApiWishListController] registerWishProduct()");

        wishListService.registerWishProduct(authorization, requestBody);

        return ResponseEntity.status(HttpStatus.CREATED).body("WishProduct registered successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWishProduct(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        System.out.println("[ApiWishListController] deleteWishProduct()");

        wishListService.deleteWishProduct(authorization, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("delete WishProduct successfully");
    }
}
