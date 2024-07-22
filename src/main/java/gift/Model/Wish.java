package gift.Model;

import jakarta.persistence.*;

@Entity
public class Wish {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;
    @Column(nullable = false)
    int count;

    protected Wish(){}

    public Wish(Member member, Product product, int count) {
        validateMember(member);
        validateProduct(product);
        validateCount(count);

        this.member = member;
        this.product = product;
        this.count = count;
    }

    public void validateMember(Member member){
        if (member == null)
            throw new IllegalArgumentException("wish에 member는 필수입니다");
    }

    public void validateProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("wish에 product는 필수입니다");
    }

    public void validateCount(int count) {
        if (count < 1 )
            throw new IllegalArgumentException("wish의 count값은 1이상이여야 합니다");
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
