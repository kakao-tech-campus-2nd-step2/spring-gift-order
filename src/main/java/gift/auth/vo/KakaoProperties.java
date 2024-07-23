package gift.auth.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
        String grantType,
        String redirectUri,
        String clientId
) { }
