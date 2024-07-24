package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoOauthConfig(
        String key,
        String url
) {
    public String createURL() {
        return url.concat(key);
    }
}
