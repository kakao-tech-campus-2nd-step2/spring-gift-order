package gift.product.model;

<<<<<<< HEAD
import static gift.product.exception.GlobalExceptionHandler.CANNOT_SUBTRACT_ZERO_OR_NEGATIVE;
import static gift.product.exception.GlobalExceptionHandler.SUBTRACT_EXCEED_QUANTITY;

=======
>>>>>>> e44b601 (feat: init code)
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9 ()\\[\\]+\\-\\&/\\_]*$", message = "( ), [ ], +, -, &, /, _ 외의 특수문자는 사용이 불가합니다.")
    private String name;

    @Column(nullable = false)
    @PositiveOrZero(message = "옵션의 갯수는 음수로 설정할 수 없습니다.")
    private int quantity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    public Option() {

    }

    public Option(String name, int quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Option(Long id, String name, int quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }
<<<<<<< HEAD

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

=======
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
>>>>>>> e44b601 (feat: init code)
    public Product getProduct() {
        return product;
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }
<<<<<<< HEAD

    public void subtractQuantity(int amountToSubtract) {
        if(amountToSubtract < 1)
            throw new IllegalArgumentException(CANNOT_SUBTRACT_ZERO_OR_NEGATIVE);
        if(this.quantity < amountToSubtract)
            throw new IllegalArgumentException(SUBTRACT_EXCEED_QUANTITY);
=======
    public void subtractQuantity(int quantity) {
>>>>>>> e44b601 (feat: init code)
        this.quantity -= quantity;
    }
}
