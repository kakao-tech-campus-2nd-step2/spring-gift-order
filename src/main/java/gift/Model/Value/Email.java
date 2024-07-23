package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    @Column(nullable = false, unique = true)
    private String value;

    public Email(String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("이메일 값은 필수입니다.");
        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("이메일 양식을 다시 확인해주세요");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Email))
            return false;

        Email value = (Email) object;
        return Objects.equals(this.value, value.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
