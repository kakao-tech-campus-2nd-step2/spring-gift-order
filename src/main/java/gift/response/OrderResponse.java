package gift.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    @JsonProperty(value = "option_id")
    Long optionId,
    Integer quantity,
    @JsonProperty(value = "order_date_time")
    LocalDateTime orderDateTime,
    String message
) {

}
