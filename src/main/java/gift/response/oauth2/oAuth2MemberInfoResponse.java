package gift.response.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record oAuth2MemberInfoResponse(
    String id,
    @JsonProperty("connected_at")
    LocalDateTime connectedAt
) {

}
