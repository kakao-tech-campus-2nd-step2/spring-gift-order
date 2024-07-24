package gift.auth.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;

@ConfigurationProperties("kakao")
public record KakaoProperties(
        String grantType,
        String redirectUri,
        String clientId,
        String authorizationPrefix
) {
    public LinkedMultiValueMap<String, String> getRequestBody(String code) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", this.grantType);
        requestBody.add("client_id", this.clientId);
        requestBody.add("redirect_uri", this.redirectUri);
        requestBody.add("code", code);
        return requestBody;
    }

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + this.redirectUri +
                "&client_id="    + this.clientId;
    }

}
