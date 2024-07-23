package gift.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    @Value("${client-id}") String clientId,
    @Value("${redirect-uri}") String redirectUri
) {

}
