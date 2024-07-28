package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Product;
import gift.entity.WishlistDTO;
import gift.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {


    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping()
    public List<Product> getWishlist(HttpServletRequest request,
                                     Pageable pageable) {
        String email = (String) request.getAttribute("email");
        return wishlistService.getWishlistProducts(email, pageable).getContent();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<MessageResponseDTO> postWishlist(HttpServletRequest request, @RequestBody @Valid WishlistDTO form) {
        String email = (String) request.getAttribute("email");
        wishlistService.addWishlistProduct(email, form);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Product added to wishlist successfully"));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<MessageResponseDTO> deleteWishlist(@PathVariable("id") Long id, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        wishlistService.deleteWishlist(email, id);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Wishlist deleted successfully"));
    }

    private MessageResponseDTO makeMessageResponse(String message) {
        return new MessageResponseDTO(message);
    }
}
