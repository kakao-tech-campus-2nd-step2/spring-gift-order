package gift.order;

import gift.common.model.BaseEntity;
import gift.member.model.Member;
import gift.option.model.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;
    @ManyToOne
    @JoinColumn(name = "option_id")
    Option option;
    Integer quantity;
    String message;

    public Order(Member member, Option option, Integer quantity, String message) {
        this.member = member;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Long getOptionId() {
        return option.getId();
    }

    public String createOrderText() {
        return String.format("""
            -주문 내역-
            주문 카테고리 : %s
            주문 상품 : %s
            주문 수량 : %d
                        
            메세지 : %s
            """,
            option.getProduct().getCategory(),
            option.getProduct().getName(),
            quantity,
            message);
    }
}
