package gift.api.member;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String grantType,
    String clientId,
    String redirectUrl
) {}
