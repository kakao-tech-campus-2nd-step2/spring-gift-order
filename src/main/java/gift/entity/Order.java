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
@Table(name= "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Order() {
    }

    public Long getId() {
        return id;
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

    public Option getOption() {
        return option;
    }

    public Member getMember() {
        return member;
    }
}