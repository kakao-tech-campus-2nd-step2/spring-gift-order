package gift.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RequestWishDTO {
    @NotNull
    @Min(value = 1, message = "상품Id값은 최소 1이상입니다")
    private Long productId;

    @Min(value = 1, message = "Wish의 count값은 최소 1이상이여야 합니다")
    private int count;

    public RequestWishDTO() {
    }

    public RequestWishDTO(Long productId, int count) {
        this.productId = productId;
        this.count = count;
    }

    public Long getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
