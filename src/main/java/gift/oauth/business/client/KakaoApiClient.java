package gift.oauth.business.client;

import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.dto.OAuthProvider;
import gift.oauth.business.dto.OauthToken;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient implements OAuthApiClient {

    private final RestClient restClient;

    public KakaoApiClient() {
        this.restClient = RestClient.create();
    }

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getAccessToken(OAuthParam param) {
        var url = "https://kauth.kakao.com/oauth/token";

        var result =  restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(param.getTokenRequestBody())
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (request, response) -> {
                    throw new RuntimeException("Failed to get access token from Kakao API.");
                }
            )
            .body(OauthToken.Kakao.class);

        System.out.println(result);

        return result.access_token();
    }
}
