package gift.domain;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long option_id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    private int quantity;

    private String message;

    private LocalDate ordered_at;

    public Order(Member member, Product product, Option option, Integer quantity, String message) {
        this.member = member;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        this.ordered_at = LocalDate.now(); // 현재 날짜를 설정
    }
}
