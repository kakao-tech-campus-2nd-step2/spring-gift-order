package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderProductOption extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    protected OrderProductOption() {
    }

    public OrderProductOption(Order order, Product product, Option option) {
        this.order = order;
        this.product = product;
        this.option = option;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Option getOption() {
        return option;
    }
}
