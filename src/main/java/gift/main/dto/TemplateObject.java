package gift.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TemplateObject(
        @JsonProperty("object_type")
        String objectType,
        String text,
        Link link
) {

    record Link(String wep_url) {
    }

    public TemplateObject(@JsonProperty("object_type")
                          String objectType, String text, String link) {
        this(objectType, text, new Link(link));
    }
}
