package gift.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoService {
    private final KakaoOauthProperty kakaoOauthProperty;
    private final RestClient restClient;

    public KakaoService(KakaoOauthProperty kakaoOauthProperty, RestClient restClient) {
        this.kakaoOauthProperty = kakaoOauthProperty;
        this.restClient = restClient;
    }

    public String getKakaoRedirectUrl() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthProperty.clientId())
                .queryParam("redirect_uri", kakaoOauthProperty.redirectUri())
                .queryParam("scope", String.join(",", kakaoOauthProperty.scope()).replace("\"", ""))
                .toUriString();
    }

    public KakaoToken fetchToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("redirect_uri", kakaoOauthProperty.redirectUri());
        formData.add("code", code);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(KakaoToken.class);
    }

    public KakaoToken refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", kakaoOauthProperty.clientId());
        formData.add("refresh_token", refreshToken);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(KakaoToken.class);
    }

    public KakaoResponse fetchMemberInfo(String accessToken) {
        return restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(KakaoResponse.class);
    }

    public void unlink(Long userId) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", String.valueOf(userId));

        restClient.post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoOauthProperty.adminKey())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(String.class);
    }
}
