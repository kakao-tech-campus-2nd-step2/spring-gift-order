package gift.order.dto;

public class CreateOrderRequestDTO {
    private long optionId;
    private int quantity;
    private String message;

    public long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
