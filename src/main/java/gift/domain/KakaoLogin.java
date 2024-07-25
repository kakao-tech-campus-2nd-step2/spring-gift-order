package gift.domain;

import gift.constants.ErrorMessage;
import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserInfo;
import gift.entity.Member;
import gift.exception.KakaoLoginBadRequestException;
import gift.exception.KakaoLoginForbiddenException;
import gift.exception.KakaoLoginUnauthorizedException;
import gift.properties.KakaoProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLogin {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();

    public KakaoLogin(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoUserInfo getUserInfo(KakaoTokenResponse response) {
        return restClient.post()
            .uri(kakaoProperties.getUserInfoUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + response.getAccessToken())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                switch (res.getStatusCode().value()) {
                    case 400:
                        throw new KakaoLoginBadRequestException(
                            ErrorMessage.KAKAO_BAD_REQUEST_MSG);
                    case 401:
                        throw new KakaoLoginUnauthorizedException(
                            ErrorMessage.KAKAO_UNAUTHORIZED_MSG);
                    case 403:
                        throw new KakaoLoginForbiddenException(
                            ErrorMessage.KAKAO_FORBIDDEN_MSG);
                }
            })
            .body(KakaoUserInfo.class);
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        return restClient.post()
            .uri(kakaoProperties.getTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(generateGetTokenRequestBody(code))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                switch (res.getStatusCode().value()) {
                    case 400:
                        throw new KakaoLoginBadRequestException(
                            ErrorMessage.KAKAO_BAD_REQUEST_MSG);
                    case 401:
                        throw new KakaoLoginUnauthorizedException(
                            ErrorMessage.KAKAO_UNAUTHORIZED_MSG);
                    case 403:
                        throw new KakaoLoginForbiddenException(
                            ErrorMessage.KAKAO_FORBIDDEN_MSG);
                }
            })
            .body(KakaoTokenResponse.class);
    }

    public Member generateMemberByKakaoUserInfo(KakaoUserInfo kakaoUserInfo) {
        return new Member(
            kakaoUserInfo.getId() + "@kakao.com",
            Long.toString(kakaoUserInfo.getId())
        );
    }

    private LinkedMultiValueMap<String, String> generateGetTokenRequestBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        return body;
    }
}
