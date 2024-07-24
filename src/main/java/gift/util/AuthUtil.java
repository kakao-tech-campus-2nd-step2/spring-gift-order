package gift.util;

import gift.config.KakaoProperties;
import gift.dto.OAuth.AuthTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class AuthUtil {

    private final KakaoProperties kakaoProperties;

    @Autowired
    public AuthUtil(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String createGetCodeUrl() {
        String authUrl = kakaoProperties.getAuthUrl();

        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id", kakaoProperties.getRestAPiKey())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .queryParam("response_type", "code")
                .toUriString();
        return url;
    }


    public String getAccessToken(String authCode) {
        RestClient restClient = RestClient.builder().build();

        String url = kakaoProperties.getTokenUrl();
        MultiValueMap<String, String> params = createParams(authCode);
        AuthTokenResponse resp = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(AuthTokenResponse.class);

        return resp.accessToken();
    }

    public String extractUserEmail(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestClient restClient = RestClient.builder().build();
        Map resp = restClient.get()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);
        Map<String, Object> accountMap = (Map<String, Object>) resp.get("kakao_account");
        return (String) accountMap.get("email");
    }

    private MultiValueMap<String, String> createParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getRestAPiKey());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", authCode);
        return params;
    }


}
