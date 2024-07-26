package gift.dto.request;

public class OrderRequest {

    private Long optionId;
    private int quantity;
    private String message;

    public OrderRequest() { }

    public OrderRequest(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

}
