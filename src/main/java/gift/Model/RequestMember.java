package gift.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RequestMember(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "이메일 주소 양식을 확인해주세요"
        )
        String email,

        @NotBlank
        String password
) {}


