package gift.entity;

import gift.value.OptionQuantity;
import jakarta.persistence.*;

@Entity
@Table(name = "option")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {}

    public Option(String name, Product product, int quantity) {
        this.name = name;
        this.product = product;
        this.quantity = quantity;
    }

    public void assignToProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("수량은 0개 이상이어야 합니다.");
        }
        this.product = product;
    }

    public void removeFromProduct() {
        this.product = null;
    }

    public void subtractQuantity(int quantityToSubtract) {
        OptionQuantity optionQuantity = new OptionQuantity(this.quantity);
        this.quantity = optionQuantity.subtract(quantityToSubtract).getQuantity();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}