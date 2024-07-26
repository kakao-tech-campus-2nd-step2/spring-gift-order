package gift.domain.service;

import gift.domain.dto.response.OauthTokenResponse;
import gift.domain.exception.unauthorized.TokenUnexpectedErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OauthService {

    @Value("${oauth.kakao.api_key}")
    private String KAKAO_API_KEY;

    @Value("${oauth.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    public OauthTokenResponse getOauthToken(String authorizationCode) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", KAKAO_API_KEY);
            body.add("redirect_uri", KAKAO_REDIRECT_URI);
            body.add("code", authorizationCode);

            return RestClient.builder().build()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(OauthTokenResponse.class)
                .getBody();

        } catch (Exception e) {
            throw new TokenUnexpectedErrorException();
        }
    }
}
