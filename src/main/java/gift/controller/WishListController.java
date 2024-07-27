package gift.controller;

import static gift.util.JwtUtil.extractToken;

import gift.domain.Product;
import gift.domain.WishList;
import gift.error.UnauthorizedException;
import gift.service.WishListService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final JwtUtil jwtUtil;

    public WishListController(WishListService wishListService, JwtUtil jwtUtil) {
        this.wishListService = wishListService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<?> getWishListItems(HttpServletRequest request) {
        Long memberId = jwtUtil.extractMemberId(request);
        List<WishList> wishLists = wishListService.getWishListItems(memberId);
        return ResponseEntity.ok(wishLists);
    }

    @GetMapping("/page")
    public ResponseEntity<?> getWishListItems(
        HttpServletRequest request,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        Long memberId = jwtUtil.extractMemberId(request);
        Page<WishList> wishLists = wishListService.getWishListItems(memberId, page, size);
        return ResponseEntity.ok(wishLists);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addWishListItem(HttpServletRequest request,
        @Valid @RequestBody Product product) {
        Long memberId = jwtUtil.extractMemberId(request);
        WishList wishList = new WishList(memberId, product.getId());
        wishListService.addWishListItem(wishList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to wishlist");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteWishListItem(@PathVariable("id") Long id) {
        wishListService.deleteWishListItem(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product removed from wishlist");
    }

}
