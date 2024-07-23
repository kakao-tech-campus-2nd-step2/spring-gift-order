package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Description {

    @Column(nullable = false)
    private String description;

    public Description(String description) {
        validateDescription(description);

        this.description = description;
    }

    private void validateDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("description의 값은 null이 올 수 없습니다");
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Description))
            return false;

        Description description = (Description) object;
        return Objects.equals(this.description, description.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
