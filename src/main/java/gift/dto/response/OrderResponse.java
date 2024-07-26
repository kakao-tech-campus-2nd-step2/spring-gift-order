package gift.dto.response;

import java.time.LocalDateTime;


public class OrderResponse {
    
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderTime;
    private String message;

    public OrderResponse(Long id, Long optionId, int quantity, String message, LocalDateTime orderTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderTime = orderTime;
    }
}
