package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Count {

    private int value;

    public Count(int value) {
        validateCount(value);

        this.value = value;
    }

    private void validateCount(int value){
        if (value < 1)
            throw new IllegalArgumentException("count값은 1이상이여야 합니다");
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Count))
            return false;

        Count value = (Count) object;
        return Objects.equals(this.value, value.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
