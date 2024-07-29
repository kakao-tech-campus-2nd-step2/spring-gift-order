package gift.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.WishlistRequest;
import gift.dto.WishlistResponse;
import gift.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
	
	private final WishlistService wishlistService;
	
	public WishlistController(WishlistService wishlistService) {
		this.wishlistService = wishlistService;
	}

	@GetMapping
	public ResponseEntity<Page<WishlistResponse>> getWishlist(@RequestHeader("Authorization") String token,
			@PageableDefault(sort = "product.name") Pageable pageable) {
		Page<WishlistResponse> wishlist = wishlistService.getWishlist(token, pageable);
		if (wishlist.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(wishlist);
	}

	@PostMapping
	public ResponseEntity<Void> addWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.addWishlist(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping
	public ResponseEntity<Void> removeWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.removeWishlist(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping
	public ResponseEntity<Void> updateWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.updateWishlistQuantity(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}