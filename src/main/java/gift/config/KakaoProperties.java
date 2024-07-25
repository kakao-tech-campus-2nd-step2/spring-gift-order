package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String redirectUri,
        String restAPiKey,
        String tokenUrl,
        String authUrl,
        String userInfoUrl,
        String sendMessageUrl
) {
}