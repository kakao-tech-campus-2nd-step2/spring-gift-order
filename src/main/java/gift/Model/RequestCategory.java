package gift.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestCategory(
        @NotBlank
        String name,

        @NotBlank
        @Size(min=7, max=7, message = "카테고리 색상 값의 길이는 7입니다")
        String color,

        @NotBlank
        String imageUrl,

        @NotNull
        String description
){ }
