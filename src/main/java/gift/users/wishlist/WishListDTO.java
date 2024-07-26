package gift.users.wishlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishListDTO {

    private long userId;
    @NotNull(message = "상품 아이디를 입력하지 않았습니다.")
    @Min(value = 1, message = "상품 아이디는 1 이하일 수 없습니다.")
    private long productId;
    @NotNull(message = "상품 수량을 입력하지 않았습니다.")
    @Min(value = 1, message = "상품 수량은 1 이하일 수 없습니다.")
    private int num;
    @NotNull(message = "옵션 아이디를 입력하지 않았습니다.")
    @Min(value = 1, message = "옵션 아이디는 1 이하일 수 없습니다.")
    private long optionId;

    public WishListDTO() {
    }

    public WishListDTO(long userId, long productId, int num, long optionId) {
        this.userId = userId;
        this.productId = productId;
        this.num = num;
        this.optionId = optionId;
    }

    public long getProductId() {
        return productId;
    }

    public long getOptionId() {
        return optionId;
    }

    public int getNum() {
        return num;
    }

    public static WishListDTO fromWishList(WishList wishList) {
        return new WishListDTO(wishList.getUser().getId(), wishList.getProduct().getId(),
            wishList.getNum(), wishList.getOption().getId());
    }
}
