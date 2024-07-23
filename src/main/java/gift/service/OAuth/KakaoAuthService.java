package gift.service.OAuth;

import gift.config.KakaoProperties;
import gift.dto.OAuth.AuthTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;

    @Autowired
    public KakaoAuthService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String createCodeUrl(){
        String authUrl = kakaoProperties.getAuthUrl();

        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id",kakaoProperties.getRestAPiKey())
                .queryParam("redirect_uri",kakaoProperties.getRedirectUri())
                .queryParam("response_type","code")
                .toUriString();
        return url;
    }

    public AuthTokenResponse getAccessToken(String authCode) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();

        String url = kakaoProperties.getTokenUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getRestAPiKey());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", authCode);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<AuthTokenResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                AuthTokenResponse.class);

        return response.getBody();
    }
}
