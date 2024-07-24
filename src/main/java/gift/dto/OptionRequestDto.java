package gift.dto;

import gift.validation.ValidName;
import gift.vo.Option;
import gift.vo.Product;
import jakarta.validation.constraints.*;

public record OptionRequestDto(
    Long id,

    @NotNull
    Long productId,

    @ValidName
    @NotBlank(message = "옵션명을 입력해 주세요.")
    String name,

    @NotNull
    @PositiveOrZero(message = "옵션 수량은 0개 이상이여야 합니다.")
    int quantity
){
    public Option toOption (Product product) {
        return new Option(id, product, name, quantity);
    }
}