package gift.dto.request;

public class OrderRequest {

    private Long optionId;
    private Integer quantity;
    private String message;
    private Long receiveMemberId;

    public OrderRequest() { }

    public OrderRequest(Long optionId, int quantity, String message, Long receiveMemberId) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.receiveMemberId = receiveMemberId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getReceiveMemberId() {
        return receiveMemberId;
    }

    public String getMessage() {
        return message;
    }
}
