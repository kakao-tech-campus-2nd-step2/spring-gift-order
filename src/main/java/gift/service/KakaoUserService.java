package gift.service;

import gift.domain.KakaoProperties;
import gift.exception.KakaoOAuthException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class KakaoUserService {
    private final KakaoProperties properties;
    private final RestClient client;

    public KakaoUserService(KakaoProperties properties) {
        this.properties = properties;
        this.client = RestClient.builder().build();
    }

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
            + properties.redirectUrl() + "&client_id=" + properties.clientId();
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        try {
            var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }
}
