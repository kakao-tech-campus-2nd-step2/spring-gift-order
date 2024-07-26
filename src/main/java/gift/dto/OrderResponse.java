package gift.dto;

import java.time.LocalDate;

public class OrderResponse {
    private Long id;
    private Long optionId;
    private int quantity;
    private String orderDateTime;
    private String message;

    public OrderResponse(Long id, Long optionId, int quantity, LocalDate orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }
}
