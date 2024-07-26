package gift.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    private int quantity;

    private LocalDateTime orderDateTime;

    private String message;

    public Order() {
    }

    public Order(Member member, Option option, int quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
    }

}
