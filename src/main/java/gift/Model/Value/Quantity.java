package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Quantity {
    private int quantity;

    public Quantity(int quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("수량은 최소 1개 이상이여야 합니다");
        if (quantity > 9999_9999)
            throw new IllegalArgumentException("수량은 최대 1억개 미만이여야 합니다");

    }

    public void subtract(int quantity){
        if (this.quantity - quantity < 1)
            throw new IllegalArgumentException("옵션 수량은 최소 1개이상이여야 합니다. 빼려는 수량을 조절해 주십시오.");

        this.quantity -= quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Quantity))
            return false;

        Quantity quantity = (Quantity) object;
        return Objects.equals(this.quantity,quantity.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
