package gift.controller;

import gift.dto.WishDto;
import gift.service.JwtUtil;
import gift.service.WishlistService;
import gift.vo.Wish;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @GetMapping("/wishlist")
    public ResponseEntity<Page<WishDto>> getWishProductList(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        Long memberId = JwtUtil.getBearTokenAndMemberId(authorizationHeader);

        Page<Wish> allWishlistsPaged = service.getWishProductList(memberId, pageNumber-1, pageSize);

        return new ResponseEntity<>(allWishlistsPaged.map(WishDto::toWishDto), HttpStatus.OK);
    }

    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<Void> addToWishlist(@PathVariable("productId") Long productId, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = JwtUtil.getBearTokenAndMemberId(authorizationHeader);

        service.addWishProduct(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist/{wishProductId}")
    public ResponseEntity<Void> deleteToWishlist(@PathVariable("wishProductId") Long wishProductId, @RequestHeader("Authorization") String authorizationHeader) {
        service.deleteWishProduct(wishProductId);

        return ResponseEntity.ok().build();
    }

}
