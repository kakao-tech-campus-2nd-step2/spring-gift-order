package gift.model.order;

public class OrderForm {

    private final Long targetId;
    private final Long itemId;
    private final Long optionId;
    private final Long quantity;
    private final String message;

    public OrderForm(Long targetId, Long itemId, Long optionId, Long quantity, String message) {
        this.targetId = targetId;
        this.itemId = itemId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
