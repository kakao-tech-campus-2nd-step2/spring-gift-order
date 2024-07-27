package gift.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestOrderDTO (
    @NotNull(message = "옵션을 선택하지 않으셨습니다. 선택해주세요")
    @Min(value = 1, message = "옵션Id 값은 1이상입니다")
    Long optionId,

    @NotNull(message = "수량을 입력하지 않으셨습니다. 입력해주세요")
    @Min(value = 1, message = "수량은 적어도 1이상입니다")
    Integer quantity,

    String message
) { }
