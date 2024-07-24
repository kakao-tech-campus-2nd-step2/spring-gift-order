package gift.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TemplateObject(
        @JsonProperty("object_type")
        String objectType,
        String text,
        Link link
) {
}
