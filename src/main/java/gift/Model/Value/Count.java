package gift.Model.Value;

import jakarta.persistence.Embeddable;

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
}
