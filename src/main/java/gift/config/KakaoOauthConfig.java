package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@ConfigurationProperties(prefix = "kakao")
public record KakaoOauthConfig(
        String loginUrl,
        String tokenUrl,
        String userInfoUrl,
        String clientId,
        String redirection
) {
    public String createLoginUrl() {
        return loginUrl.formatted(redirection, clientId);
    }

    public RestClient createTokenClient() {
        return RestClient.create(tokenUrl);
    }

    public RestClient createUserInfoClient() {
        return RestClient.create(userInfoUrl);
    }

    public MultiValueMap<String, String> createBody(String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", clientId);
        requestBody.add("redirect_uri", redirection);
        requestBody.add("code", code);

        return requestBody;
    }
}
