package gift.auth.controller;

import gift.auth.dto.KakaoProperties;
import gift.auth.dto.KakaoTokens;
import gift.auth.dto.KakaoUserInfo;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient {

    private final static String KAKAO_API_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final static String BEARER_PREFIX = "Bearer ";

    private final RestClient restClient = RestClient.builder().build();

    private final KakaoProperties kakaoProperties;

    public KakaoApiClient(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> body = kakaoProperties.makeBody(code);

        var result = restClient.post()
                .uri(KAKAO_API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(KakaoTokens.class);

        return Objects.requireNonNull(result.getBody()).accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return restClient.get()
                .uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoUserInfo.class)
                .getBody();
    }
}
