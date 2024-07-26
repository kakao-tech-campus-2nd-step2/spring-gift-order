package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record KakaoProperties(
    @Value("${client-id}") String clientId,
    @Value("${redirect-uri}") String redirectUri,
    @Value("token-url") String tokenUrl,
    @Value("user-info-url") String userInfoUrl,
    @Value("message-url") String messageUrl
) {

}
