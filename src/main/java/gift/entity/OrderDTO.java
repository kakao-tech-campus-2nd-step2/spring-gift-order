package gift.entity;

public class OrderDTO {
    private Long productId;
    private Long optionId;
    private int quantity;
    private String message;

    public OrderDTO() {
    }

    public OrderDTO(Long productId, Long optionId, int quantity, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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

    public String getMessage() {
        return message;
    }
}
