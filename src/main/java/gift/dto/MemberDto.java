package gift.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class MemberDto {
    @Email
    private String email;
    private String password;
    private String kakaoId;
}
