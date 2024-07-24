package gift.Util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record KakaoProperties(
        String clientId,
        String redirectUrl
) {
}
