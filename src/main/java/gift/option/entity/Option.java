package gift.option.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    private String name;

    private int quantity;

    // 옵션이라는 것이 어떤 제품에 종속적이라고 생각해서 Option에도 productId를 추가하였습니다.
    // ex. 검정 반팔티의 옵션 (검정 반팔티 S, 검정 반팔티 M, 검정 반팔티 L, 검정 반팔티 XL)
    @Column(name = "product_id")
    private long productId;

    public Option(Long optionId, String name, int quantity, long productId) {
        this.optionId = optionId;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public Option(String name, int quantity, long productId) {
        this(null, name, quantity, productId);
    }

    protected Option() {

    }

    // 수량 차감 메서드
    public void subtractQuantity() {
        if (quantity < 1) {
            throw new IllegalArgumentException("품절된 상품입니다.");
        }

        quantity--;
    }

    // 아직은 쓰진 않지만, 언젠간 필요할 것 같아서 만든 물량 채우기 메서드
    public void fillQuantity(int quantity) {
        this.quantity += quantity;
    }

    public Long getOptionId() {
        return optionId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getproductId() {
        return productId;
    }
}
