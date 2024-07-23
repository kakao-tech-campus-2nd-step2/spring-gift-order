package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Quantity {
    private int value;

    public Quantity(int value) {
        validateQuantity(value);

        this.value = value;
    }

    private void validateQuantity(int value) {
        if (value <= 0)
            throw new IllegalArgumentException("수량은 최소 1개 이상이여야 합니다");
        if (value > 9999_9999)
            throw new IllegalArgumentException("수량은 최대 1억개 미만이여야 합니다");

    }

    public void subtract(int value){
        if (this.value - value < 1)
            throw new IllegalArgumentException("옵션 수량은 최소 1개이상이여야 합니다. 빼려는 수량을 조절해 주십시오.");

        this.value -= value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Quantity))
            return false;

        Quantity value = (Quantity) object;
        return Objects.equals(this.value, value.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
