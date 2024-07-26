package gift.dto;

public class OrderItemRequest {
    private Long productId;
    private Long optionId;
    private int quantity;

    public OrderItemRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

}
