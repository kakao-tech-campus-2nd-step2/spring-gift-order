package gift.wishlist;

import gift.member.MemberTokenResolver;
import gift.product.Product;
import gift.token.MemberTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wishlist", description = "Wishlist API")
@RestController
@RequestMapping("/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "사용자의 토큰을 통해 위시리스트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "403", description = "잘못된 유저 토큰")
    public List<Product> getAllWishlists(@MemberTokenResolver MemberTokenDTO memberTokenDTO) {
        return wishlistService.getAllWishlists(memberTokenDTO);
    }

    @PostMapping("/{product_id}")
    @Operation(summary = "위시리스트 추가", description = "사용자의 위시리스트에 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "추가 성공")
    @ApiResponse(responseCode = "400", description = "이미 위시리스트에 존재하는 상품")
    @ApiResponse(responseCode = "403", description = "잘못된 유저 토큰")
    public void addWishlist(
        @MemberTokenResolver MemberTokenDTO memberTokenDTO,
        @PathVariable(name = "product_id") long productId
    ) {
        wishlistService.addWishlist(memberTokenDTO, productId);
    }

    @DeleteMapping("/{product_id}")
    @Operation(summary = "위시리스트 삭제", description = "사용자의 위시리스트에서 해당 상품을 제거합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 위시리스트")
    @ApiResponse(responseCode = "403", description = "잘못된 유저 토큰")
    public void deleteWishlist(
        @MemberTokenResolver MemberTokenDTO memberTokenDTO,
        @PathVariable(name = "product_id") long productId
    ) {
        wishlistService.deleteWishlist(memberTokenDTO, productId);
    }
}
