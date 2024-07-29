package gift.domain.menu;

import jakarta.validation.constraints.Pattern;

public record MenuRequest(
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = " ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다")
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {

}
