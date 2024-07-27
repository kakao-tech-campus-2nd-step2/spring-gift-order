package gift.DTO;

import gift.Model.Entity.Order;

public class ResponseOrderDTO {
    private Long id;
    private Long optionId;
    private Integer quantity;
    private String orderDateTime;
    private String message;

    public ResponseOrderDTO(Long id, Long optionId, Integer quantity, String orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseOrderDTO of(Order order){
        return new ResponseOrderDTO(order.getId(), order.getOption().getId(), order.getQuantity().getValue(), order.getOrderDateTime().toString(), order.getMessage());
    }

}
