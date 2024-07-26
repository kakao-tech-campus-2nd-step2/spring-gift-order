package gift.api.order.dto;

public record TemplateObject(
    String objectType,
    String text,
    Link link,
    String buttonTitle
) {

    public static TemplateObject of(String text, String webUrl, String mobileWebUrl, String buttonTitle) {
        return new TemplateObject("text", text, Link.of(webUrl, mobileWebUrl), buttonTitle);
    }
}
