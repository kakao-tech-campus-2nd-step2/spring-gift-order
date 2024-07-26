package gift.web.dto.request.member;

import gift.domain.Member;
import jakarta.validation.constraints.Email;

public class CreateMemberRequest {

    @Email
    private String email;

    private String name;

    public CreateMemberRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Member toEntity() {
        return new Member.Builder()
            .email(gift.domain.vo.Email.from(this.email))
            .name(this.name)
            .build();
    }
}
