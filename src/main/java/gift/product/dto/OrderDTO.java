package gift.product.dto;

import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import jakarta.validation.constraints.Positive;

public class OrderDTO {

    @Positive
    private int quantity;
    private String message;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order convertToDomain(Option option, Member orderer) {
        return new Order(
            option,
            quantity,
            message,
            orderer
        );
    }

}
