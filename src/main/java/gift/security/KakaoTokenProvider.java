package gift.security;

import gift.DTO.KakaoProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoTokenProvider {
    private final KakaoProperties kakaoProperties;
    private final RestClient client = RestClient.builder().build();

    public KakaoTokenProvider(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public ResponseEntity<String> getToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_url", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        return client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
    }
}
