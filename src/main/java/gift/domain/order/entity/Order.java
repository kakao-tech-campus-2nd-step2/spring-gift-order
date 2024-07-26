package gift.domain.order.entity;

import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column
    private String message;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderDateTime;

    protected Order() {
    }

    public Order(Long id, User user, Product product, Option option, int quantity, String message) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }
}
