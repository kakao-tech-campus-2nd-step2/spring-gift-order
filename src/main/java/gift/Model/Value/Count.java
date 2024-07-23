package gift.Model.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Count {

    private int count;

    public Count(int count) {
        validateCount(count);

        this.count = count;
    }

    private void validateCount(int count){
        if (count < 1)
            throw new IllegalArgumentException("count값은 1이상이여야 합니다");
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Count))
            return false;

        Count count = (Count) object;
        return Objects.equals(this.count, count.getCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
