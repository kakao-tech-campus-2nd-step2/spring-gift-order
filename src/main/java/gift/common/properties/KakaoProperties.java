package gift.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String clientId,
        String redirectUrl,
        String adminRedirectUrl,
        String tokenUrl,
        String memberInfoUrl,
        String loginUrl,
        String logoutUrl,
        String refreshUrl
) {}
