package gift.Model.Value;

import jakarta.persistence.Embeddable;

@Embeddable
public class Price {
    private int price;

    public Price(int price) {
        validatePrice(price);

        this.price = price;
    }

    private void validatePrice(int price) {
        if (price < 0)
            throw new IllegalArgumentException("가격은 0원 이상이여야 합니다");

    }

    public int getPrice() {
        return price;
    }
}
