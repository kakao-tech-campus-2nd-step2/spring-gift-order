package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {

    @Column(nullable = false)
    private String value;

    public Password(String value) {
        validatePassword(value);

        this.value = value;
    }

    private void validatePassword(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("패스워드 값은 필수입니다");

    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Password))
            return false;

        Password value = (Password) object;
        return Objects.equals(this.value, value.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
