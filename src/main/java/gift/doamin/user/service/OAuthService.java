package gift.doamin.user.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.doamin.user.dto.KakaoOAuthTokenResponseDto;
import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;
import gift.doamin.user.util.AuthorizationOAuthUriBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OAuthService {

    private final KakaoClientProperties clientProperties;
    private final KakaoProviderProperties providerProperties;

    public OAuthService(KakaoClientProperties clientProperties,
        KakaoProviderProperties providerProperties) {
        this.clientProperties = clientProperties;
        this.providerProperties = providerProperties;
    }

    public String getAuthUrl() {
        return new AuthorizationOAuthUriBuilder()
            .clientProperties(clientProperties)
            .providerProperties(providerProperties)
            .build();
    }

    public String getAccessToken(String authorizeCode) {
        RestClient restClient = RestClient.builder().build();

        String tokenUri = providerProperties.tokenUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizeCode);
        body.add("redirect_uri", clientProperties.redirectUri());
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());

        ResponseEntity<KakaoOAuthTokenResponseDto> entity = restClient.post()
            .uri(tokenUri)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoOAuthTokenResponseDto.class);

        return entity.getBody().getAccess_token();
    }
}
