package gift.dto;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull
    private Long optionId;
    @NotNull
    private Integer quantity;
    private String message;

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
