package gift.dto;

public class CreateOrderDto {
    private Long member_id;
    private Long product_id;
    private Long option_id;
    private Integer quantity;
    private String message;

    public Long getMemberId() {
        return member_id;
    }

    public Long getProductId() {
        return product_id;
    }

    public Long getOptionId() {
        return option_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
