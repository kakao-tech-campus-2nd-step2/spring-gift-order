package gift.login.service;

import gift.login.dto.KakaoTokenResponseDTO;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class LoginService {
    private final RestClient client;

    private final String clientId;
    private final String redirectUri;

    public LoginService(@Value("${kakao.client-id}") String clientId,
        @Value("${kakao.redirect-uri}") String redirectUri){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        client = RestClient.builder().build();
    }

    public String getAccessToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(code);
        var response =  client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoTokenResponseDTO.class);
        return response.getBody().getAccessToken();
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

}
