package gift.doamin.order.dto;

import gift.doamin.order.entity.Order;
import java.time.LocalDateTime;

public class OrderResponse {

    Long id;
    Long optionId;
    Integer quantity;
    String orderDateTime;
    String message;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.optionId = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.orderDateTime = order.getOrderDateTime().toString();
        this.message = order.getMessage();
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
}
