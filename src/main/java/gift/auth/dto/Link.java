package gift.auth.dto;

public record Link (
        String webUrl,
        String mobileUrl
) {
    public Link(String url) {
        this(url, url);
    }

}
