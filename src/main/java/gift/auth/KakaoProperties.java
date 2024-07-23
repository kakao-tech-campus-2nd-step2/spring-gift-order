package gift.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    String restApiKey,
    String redirectUrl
) {

}
