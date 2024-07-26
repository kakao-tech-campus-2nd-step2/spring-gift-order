package gift.api.order.dto;

public record Link(
    String webUrl,
    String mobileWebUrl
) {

    public static Link of(String webUrl, String mobileWebUrl) {
        return new Link(webUrl, mobileWebUrl);
    }
}
