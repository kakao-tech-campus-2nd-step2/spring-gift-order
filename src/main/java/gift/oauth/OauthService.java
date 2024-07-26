package gift.oauth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OauthService {
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-url}")
    private String redirectUri;

    public LinkedMultiValueMap<String, String> getRequestBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        return body;
    }

    public KakaoToken getKakaoToken(String code){
        RestClient client = RestClient.builder().build();
        String url = "https://kauth.kakao.com/oauth/token";

        var body = getRequestBody(code);

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body) // request body
            .retrieve()
            .toEntity(String.class);


        return extractKakaoToken(response.getBody());
    }

    public KakaoToken extractKakaoToken(String responseBody){
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        String tokenType = jsonObject.get("token_type").getAsString();
        String refreshToken = jsonObject.get("refresh_token").getAsString();
        int expiresIn = jsonObject.get("expires_in").getAsInt();
        String scope = jsonObject.get("scope").getAsString();
        int refreshTokenExpiresIn = jsonObject.get("refresh_token_expires_in").getAsInt();

        return new KakaoToken(accessToken,tokenType,refreshToken,expiresIn,scope,refreshTokenExpiresIn);
    }
}
