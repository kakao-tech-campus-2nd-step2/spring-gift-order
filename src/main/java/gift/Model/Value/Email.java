package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private String email;

    public Email(String email) {
        validateEmail(email);
        this.email = email;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("이메일 값은 필수입니다.");
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("이메일 양식을 다시 확인해주세요");
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Email))
            return false;

        Email email = (Email) object;
        return Objects.equals(this.email, email.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
