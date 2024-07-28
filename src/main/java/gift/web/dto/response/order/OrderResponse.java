package gift.web.dto.response.order;

public class OrderResponse {

    private Long productId;

    private Long optionId;

    private Integer optionStock;

    private Integer quantity;

    private String productName;

    private String message;

    public OrderResponse(Long productId, Long optionId, Integer optionStock, Integer quantity,
        String productName, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.optionStock = optionStock;
        this.quantity = quantity;
        this.productName = productName;
        this.message = message;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getOptionStock() {
        return optionStock;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getMessage() {
        return message;
    }
}