package gift.service;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";
    @Value("${clientId}")
    private String CLIENT_ID;

    private final RestClient restClient;

    public KakaoLoginService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public KakaoTokenResponse getToken(String code) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("grant_type", GRANT_TYPE);
        multiValueMap.add("redirect_uri", REDIRECT_URI);
        multiValueMap.add("client_id", CLIENT_ID);
        multiValueMap.add("code", code);

        return restClient
                .post()
                .uri(TOKEN_REQUEST_URI)
                .body(multiValueMap)
                .retrieve()
                .body(KakaoTokenResponse.class);
    }

    public String getEmail(String token) {
        KakaoUserInfoResponse body = restClient
                .get()
                .uri(USER_INFO_REQUEST_URI)
                .header("Authorization", String.format("Bearer %s", token))
                .retrieve()
                .body(KakaoUserInfoResponse.class);

        return body.kakao_account()
                .email();
    }
}
