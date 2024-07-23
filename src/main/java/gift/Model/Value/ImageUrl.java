package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ImageUrl {

    private String value;

    protected ImageUrl() {}

    public ImageUrl(String value) {
        validateImageUrl(value);

        this.value = value;
    }

    private void validateImageUrl(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("value를 입력해주세요");

    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof ImageUrl))
            return false;

        ImageUrl value = (ImageUrl) object;
        return Objects.equals(this.value, value.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
