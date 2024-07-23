package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
record KakaoProperties(
    String clientId,
    String redirectUrl
) {

}
