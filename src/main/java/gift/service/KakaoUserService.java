package gift.service;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoUserService {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public KakaoUserService(RestTemplate restTemplate, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public String getAuthorizationUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
            + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }

    public KakaoTokenResponse getAccessToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", authorizationCode);

        RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(body,
            headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(request, KakaoTokenResponse.class);

        return response.getBody();
    }
}
