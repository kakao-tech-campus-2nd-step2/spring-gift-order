package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {

    @Column(nullable = false)
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

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Password))
            return false;

        Password password = (Password) object;
        return Objects.equals(this.password, password.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

}
