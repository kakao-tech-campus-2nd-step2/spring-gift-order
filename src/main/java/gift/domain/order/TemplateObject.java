package gift.domain.order;

public record TemplateObject(
    String object_type,
    String text,
    Link link,
    String button_title
) {

    public record Link(
        String web_url,
        String mobile_web_url
    ) {

    }

    public TemplateObject(String text) {
        this(
            "text",
            text,
            new TemplateObject.Link(
                "https://developers.kakao.com",
                "https://developers.kakao.com"
            ),
            "check button");
    }
}
