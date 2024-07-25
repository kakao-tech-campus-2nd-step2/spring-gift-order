package gift.Service;

import gift.DTO.ResponseKakaoTokenDTO;
import gift.Util.KakaoProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoLoginService {

    private static final String GENERATE_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String LOGIN_URI= "https://kauth.kakao.com/oauth/authorize";

    private final RestClient client;
    private final KakaoProperties properties;

    public KakaoLoginService(RestClient client, KakaoProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    public URI requestLogin() {
        String url = generateLoginUrl();
        URI redirectionUri = client.get()
                .uri(URI.create(url))
                .retrieve()
                .toEntity(String.class)
                .getHeaders()
                .getLocation();

        return redirectionUri;
    }

    private String generateLoginUrl() {
       return UriComponentsBuilder.fromUriString(LOGIN_URI)
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUrl())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    public String getToken(String oauthCode){
        String url = GENERATE_TOKEN_URL;
        LinkedMultiValueMap<String, String> body = generateBodyForKakaoToken(oauthCode);
        String accessToken = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(ResponseKakaoTokenDTO.class)
                .getBody()
                .getAccessToken();

        return accessToken;
    }

    private LinkedMultiValueMap<String, String> generateBodyForKakaoToken(String oauthCode) {
        LinkedMultiValueMap<String, String> body  = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", oauthCode);

        return body;
    }
}
