package gift.auth.application;

import gift.auth.dto.KakaoTokenResponse;
import gift.auth.dto.KakaoUserInfoResponse;
import gift.auth.vo.KakaoProperties;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoClient(RestTemplate restTemplate,
                       KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String authCode) {
        RequestEntity<LinkedMultiValueMap<String, String>> request = RequestEntity.post(authCode)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(kakaoProperties.getRequestBody(authCode));

        KakaoTokenResponse response = restTemplate.postForObject(
                KAKAO_TOKEN_URL,
                request,
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response.accessToken();
    }

    public Long getUserId(String token) {
        RequestEntity<Void> request = RequestEntity.get(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, kakaoProperties.authorizationPrefix() + token)
                .build();

        KakaoUserInfoResponse response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                request,
                KakaoUserInfoResponse.class
        ).getBody();

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response.id();
    }

}
