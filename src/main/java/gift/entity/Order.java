package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    private LocalDateTime orderTime;


    public Order() {}

    public Order(Option option, int quantity, String message, LocalDateTime orderTime) {
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderTime = orderTime;
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

    public LocalDateTime getOrderTime(){
        return orderTime;
    }
}
