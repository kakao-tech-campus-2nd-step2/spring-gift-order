package gift.api.member.config;

public record KakaoUrl(
    String requestFormat,
    String redirect,
    String token,
    String user
) {}
