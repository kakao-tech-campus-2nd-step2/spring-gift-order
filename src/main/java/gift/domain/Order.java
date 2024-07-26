package gift.domain;

import gift.dto.OrderDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name = "orderDateTime", nullable = false)
    private Timestamp orderDateTime;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email", nullable = false)
    private Member member;

    protected Order() {

    }

    public Order(Option option, int quantity, String message, Member member) {
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.member = member;
    }

    public OrderDTO toDTO() {
        return new OrderDTO(id, option.getId(), quantity, orderDateTime, message);
    }
}
