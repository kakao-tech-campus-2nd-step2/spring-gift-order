package gift.dto;

import gift.domain.member.Member;
import gift.dto.request.OrderCreateRequest;

public class OrderDto {

    private Member member;
    private Long optionId;
    private Long quantity;
    private String message;

    public OrderDto(Member member, Long optionId, Long quantity, String message) {
        this.member = member;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public static OrderDto of(Member member, OrderCreateRequest request) {
        return new OrderDto(member, request.getOptionId(), request.getQuantity(), request.getMessage());
    }

    public Member getMember() {
        return member;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

}
