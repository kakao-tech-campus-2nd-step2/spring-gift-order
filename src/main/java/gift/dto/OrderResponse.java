package gift.dto;

public class OrderResponse {
    private final Long id;
    private final String productName;
    private final int quantity;
    private final String userName;

    public OrderResponse(Long id, String productName, int quantity, String userName) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUserName() {
        return userName;
    }
}