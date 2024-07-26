package gift.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record OptionResponse(
        Long id,
        String name,
        Long quantity,
        Menu menu
) {
}
