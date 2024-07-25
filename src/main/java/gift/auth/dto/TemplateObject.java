package gift.auth.dto;

public record TemplateObject(
        String objectType,
        String text,
        Link link
) {
    public TemplateObject(String objectType,
                          String text,
                          String url) {
        this(objectType, text, new Link(url));
    }

}
