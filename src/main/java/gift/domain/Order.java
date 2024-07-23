package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="orders")
public class Order extends BaseEntity{
    @Column(nullable = false, name="option_id")
    private Long optionId;
    @Column(nullable = false, name="quantity")
    private Long quantity;
    @Column(nullable = false, name="message")
    private String message;
    @Column(nullable = false, name="orderDateTime")
    private String orderDateTime;

    protected Order() {
    }

    public Order(Long optionId, Long quantity, String message, String orderDateTime) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
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

    public String getOrderDateTime() {
        return orderDateTime;
    }
}
