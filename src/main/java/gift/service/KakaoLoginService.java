package gift.service;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import gift.exception.KakaoApiHasProblemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class KakaoLoginService {

    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String GRANT_TYPE = "authorization_code";
    @Value("${clientId}")
    private String clientId;

    private final RestClient restClient;

    public KakaoLoginService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public KakaoTokenResponse getToken(String code) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("grant_type", GRANT_TYPE);
        multiValueMap.add("redirect_uri", REDIRECT_URI);
        multiValueMap.add("client_id", clientId);
        multiValueMap.add("code", code);

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();
        while (retryCount < maxRetries) {
            try {
                return restClient
                        .post()
                        .uri(TOKEN_REQUEST_URI)
                        .body(multiValueMap)
                        .retrieve()
                        .body(KakaoTokenResponse.class);
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '토큰 받기' 기능에 문제가 생겼습니다.", exceptions);
    }

    public String getMemberEmail(String token) {

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();
        while (retryCount < maxRetries) {
            try {
                return restClient
                        .get()
                        .uri(USER_INFO_REQUEST_URI)
                        .header("Authorization", String.format("Bearer %s", token))
                        .retrieve()
                        .body(KakaoUserInfoResponse.class)
                        .kakaoAccount()
                        .email();
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '사용자 정보 가져오기' 기능에 문제가 생겼습니다.", exceptions);
    }
}
