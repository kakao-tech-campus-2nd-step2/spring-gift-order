package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    private int quantity;
    private String userName;

    protected Order() {
    }

    public Order(Option option, int quantity, String userName) {
        this.option = option;
        this.quantity = quantity;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }
}