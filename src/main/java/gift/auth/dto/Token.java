package gift.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record Token(@NotBlank String token) {}
