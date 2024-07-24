package gift.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull
    private Long optionId;

    @Min(1)
    @NotNull
    private Long quantity;

    @NotEmpty
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

}
