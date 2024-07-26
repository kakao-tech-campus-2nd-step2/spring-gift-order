package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WishRequest {
    private final Long productId;

    @JsonCreator
    public WishRequest(@JsonProperty("productId") Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 id를 입력해야 합니다.");
        }
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}