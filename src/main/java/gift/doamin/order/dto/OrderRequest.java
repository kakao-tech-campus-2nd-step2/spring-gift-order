package gift.doamin.order.dto;

import gift.doamin.product.entity.Option;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotNull
    Long optionId;

    @Positive
    Integer quantity;

    String message;

    public OrderRequest(Long optionId, Integer quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
