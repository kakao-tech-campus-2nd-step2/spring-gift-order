package gift.dto;

public class OrderRequestDto {
    private Long optionId;
    private Long quantity;
    private String message;

    public OrderRequestDto(Long optionId, Long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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
