package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long product_id;
    private Long option_id;
    private int quantity;
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order(OrderDTO orderDTO) {
        this.product_id = orderDTO.getProductId();
        this.option_id = orderDTO.getOptionId();
        this.quantity = orderDTO.getQuantity();
        this.message = orderDTO.getMessage();
    }

    public Order() {

    }

    public void setUser(User user) {
        this.user = user;
    }
}
