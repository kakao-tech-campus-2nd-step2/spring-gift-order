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

    private static final String KAKAO_TOKEN_PATH = "/oauth/token";
    private static final String KAKAO_USER_INFO_PATH = "/v2/user/me";

    public KakaoClient(RestTemplate restTemplate,
                       KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoTokenResponse getTokenResponse(String authCode) {
        RequestEntity<LinkedMultiValueMap<String, String>> request = RequestEntity.post(authCode)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(kakaoProperties.getRequestBody(authCode));

        KakaoTokenResponse response = restTemplate.postForObject(
                kakaoProperties.tokenDomainName() + KAKAO_TOKEN_PATH,
                request,
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response;
    }

    public Long getUserId(String token) {
        RequestEntity<Void> request = RequestEntity.get(KAKAO_USER_INFO_PATH)
                .header(HttpHeaders.AUTHORIZATION, kakaoProperties.authorizationPrefix() + token)
                .build();

        KakaoUserInfoResponse response = restTemplate.exchange(
                kakaoProperties.userInfoDomainName() + KAKAO_USER_INFO_PATH,
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
