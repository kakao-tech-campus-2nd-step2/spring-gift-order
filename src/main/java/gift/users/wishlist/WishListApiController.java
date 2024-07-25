package gift.users.wishlist;

import gift.util.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishListApiController {

    private final WishListService wishListService;

    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<WishListDTO>> getWishList(@PathVariable("userId") long userId,
        HttpServletRequest request,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        size = PageUtil.validateSize(size);
        sortBy = PageUtil.validateSortBy(sortBy, Arrays.asList("id", "productId", "num"));
        Direction direction = PageUtil.validateDirection(sortDirection);
        Page<WishListDTO> wishLists = wishListService.getWishListsByUserId(userId, page, size,
            direction, sortBy);

        return ResponseEntity.ok(wishLists);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<WishListDTO> addWishList(@PathVariable("userId") long userId,
        HttpServletRequest request, @RequestBody WishListDTO wishListDTO) {

        WishListDTO result = wishListService.addWishList(wishListDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{userId}/{productId}")
    public ResponseEntity<WishListDTO> updateWishList(@PathVariable("userId") long userId,
        @PathVariable("productId") long productId, HttpServletRequest request,
        @RequestBody WishListDTO wishListDTO) {

        WishListDTO result = wishListService.updateWishList(userId, productId,
            wishListDTO);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable("userId") long userId,
        @PathVariable("productId") long productId, HttpServletRequest request) {

        wishListService.deleteWishList(userId, productId);
        return ResponseEntity.ok().build();
    }
}
