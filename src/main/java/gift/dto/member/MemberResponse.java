package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import gift.model.RegisterType;
import jakarta.validation.constraints.NotNull;

public record MemberResponse(
    Long id,
    String email,
    String token,
    RegisterType registerType
) {

}
