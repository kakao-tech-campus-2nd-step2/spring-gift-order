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

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
