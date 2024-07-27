package gift.service;

import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserProfile;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoOAuthService {

    private final RestTemplate restTemplate;
    private final Dotenv dotenv;

    @Value("${kakao.api.tokenUrl}")
    private String tokenUrl;

    @Value("${kakao.api.userProfileUrl}")
    private String userProfileUrl;

    public KakaoOAuthService(RestTemplate restTemplate, Dotenv dotenv) {
        this.restTemplate = restTemplate;
        this.dotenv = dotenv;
    }

    public String getAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", dotenv.get("KAKAO_CLIENT_ID"));
        body.add("redirect_uri", dotenv.get("KAKAO_REDIRECT_URI"));
        body.add("code", authorizationCode);
        body.add("client_secret", dotenv.get("KAKAO_CLIENT_SECRET"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
            tokenUrl, HttpMethod.POST, request, KakaoTokenResponse.class);

        return response.getBody().getAccessToken();
    }

    public KakaoUserProfile getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserProfile> response = restTemplate.exchange(
            userProfileUrl, HttpMethod.GET, request, KakaoUserProfile.class);

        return response.getBody();
    }
}
