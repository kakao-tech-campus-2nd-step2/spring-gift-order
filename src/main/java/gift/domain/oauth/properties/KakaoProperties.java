package gift.domain.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
   String clientId,
   String redirectUrl
) {
}
