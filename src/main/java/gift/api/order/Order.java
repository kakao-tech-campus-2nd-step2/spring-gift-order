package gift.api.order;

import gift.api.option.domain.Option;
import gift.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_option_id_ref_option_id"))
    private Option option;
    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp orderDateTime;
    private String message;

    protected Order() {
    }

    public Order(Option option, String message) {
        this.option = option;
        this.message = message;
    }

    public Option getOption() {
        return option;
    }

    public Timestamp getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
