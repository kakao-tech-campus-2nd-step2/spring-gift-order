package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Long optionId;

    protected Wish() {
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public Wish(Long optionId) {
        this.optionId = optionId;
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
}