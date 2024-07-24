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
        KakaoUserInfo userInfo = restClient.post()
            .uri(kakaoProperties.getUserInfoUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + response.getAccessToken())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(400),
                (req, res) -> {
                    throw new KakaoLoginBadRequestException(ErrorMessage.KAKAO_BAD_REQUEST_MSG);
                })
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(401),
                (req, res) -> {
                    throw new KakaoLoginUnauthorizedException(ErrorMessage.KAKAO_UNAUTHORIZED_MSG);
                })
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(403),
                (req, res) -> {
                    throw new KakaoLoginForbiddenException(ErrorMessage.KAKAO_FORBIDDEN_MSG);
                })
            .body(KakaoUserInfo.class);

        return userInfo;
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        KakaoTokenResponse response = restClient.post()
            .uri(kakaoProperties.getTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(400),
                (req, res) -> {
                    throw new KakaoLoginBadRequestException(ErrorMessage.KAKAO_BAD_REQUEST_MSG);
                })
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(401),
                (req, res) -> {
                    throw new KakaoLoginUnauthorizedException(ErrorMessage.KAKAO_UNAUTHORIZED_MSG);
                })
            .onStatus(httpStatusCode -> httpStatusCode == HttpStatusCode.valueOf(403),
                (req, res) -> {
                    throw new KakaoLoginForbiddenException(ErrorMessage.KAKAO_FORBIDDEN_MSG);
                })
            .body(KakaoTokenResponse.class);

        return response;
    }

    public Member generateMemberByKakaoUserInfo(KakaoUserInfo kakaoUserInfo) {
        return new Member(
            kakaoUserInfo.getId() + "@kakao.com",
            Long.toString(kakaoUserInfo.getId())
        );
    }
}
