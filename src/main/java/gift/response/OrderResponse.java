package gift.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record OrderResponse(
    @JsonProperty(value = "order_id")
    Long id,
    @JsonProperty(value = "option_id")
    Long optionId,
    Integer quantity,
    @JsonProperty(value = "order_date_time")
    String orderDateTime,
    String message
) {

    public static OrderResponse createOrderResponse(Long id, Long optionId, Integer quantity,
        LocalDateTime orderDateTime, String message) {
        return new OrderResponse(id, optionId, quantity,
            orderDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), message);
    }
}
