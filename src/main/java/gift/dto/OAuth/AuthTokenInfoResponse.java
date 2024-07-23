package gift.dto.OAuth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthTokenInfoResponse(
        @JsonProperty("expiresInMillis") long expiresInMillis,
        @JsonProperty("id") long id,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("app_id") int appId,
        @JsonProperty("appId") int appIdDuplicate
) {}