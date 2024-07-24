package gift.domain;

import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserInfo;
import gift.entity.Member;
import gift.properties.KakaoProperties;
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
