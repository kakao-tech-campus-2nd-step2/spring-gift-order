package gift.dto.order;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    public record Create(
            @NotNull
            Long optionId,
            @NotNull
            int quantity,
            @NotNull
            String message
    ){

    }
}
