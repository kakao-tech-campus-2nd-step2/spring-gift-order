package gift.Model.Value;

import jakarta.persistence.Embeddable;

@Embeddable
public class Password {
    private String password;

    public Password(String password) {
        validatePassword(password);

        this.password = password;
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("패스워드 값은 필수입니다");

    }

    public String getPassword() {
        return password;
    }
}
