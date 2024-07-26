package gift.dto;

public record KakaoMessageRequest(
        String objType,
        String text,
        String webUrl,
        String mobileUrl
) {
}
