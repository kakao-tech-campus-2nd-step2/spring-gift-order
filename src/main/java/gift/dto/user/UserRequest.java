package gift.dto.user;

import gift.common.enums.LoginType;
import gift.model.user.User;

public class UserRequest {

    public record Create(
            String email,
            String password
    ) {
        public User toEntity() {
            return new User(this.email, this.password, LoginType.DEFAULT);
        }
    }

    public record Check(
            String email,
            String password
    ) {

    }
}
