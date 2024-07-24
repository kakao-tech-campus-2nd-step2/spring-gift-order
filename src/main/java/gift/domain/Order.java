package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Order(Long optionId, Long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.orderDateTime = now.format(formatter);
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
