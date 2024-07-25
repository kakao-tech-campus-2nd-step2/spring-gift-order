package gift.service;

import gift.config.KakaoProperties;
import gift.dto.KakaoAccessTokenDTO;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String authorizationCode) {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(authorizationCode);
        var response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessTokenDTO.class);
        KakaoAccessTokenDTO kakaoAccessTokenDTO = response.getBody();
        return kakaoAccessTokenDTO.accessToken();
    }

    private LinkedMultiValueMap<String, String> createBody(String authorizationCode) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id",kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", authorizationCode);
        return body;
    }
}