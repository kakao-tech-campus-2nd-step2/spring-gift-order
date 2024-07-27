package gift.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    private String message;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    protected Order() {
    }

    private Order(Builder builder) {
        this.option = builder.option;
        this.quantity = builder.quantity;
        this.message = builder.message;
        this.orderDateTime = builder.orderDateTime;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public static class Builder {
        private Option option;
        private int quantity;
        private String message;
        private LocalDateTime orderDateTime = LocalDateTime.now();

        public Builder(Option option, int quantity) {
            this.option = option;
            this.quantity = quantity;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder orderDateTime(LocalDateTime orderDateTime) {
            this.orderDateTime = orderDateTime;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
