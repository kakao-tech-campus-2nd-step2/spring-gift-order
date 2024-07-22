package gift.main.service;

import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoProfile;
import gift.main.dto.KakaoTokenResponse;
import gift.main.dto.UserJoinRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private static MediaType CONTENT_TYPE = MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8");

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        restClient = RestClient.create();
    }

    //카카오 인가코드를 이용한 엑세스 토큰 발급해오기
    public KakaoTokenResponse receiveKakaoToken(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", kakaoProperties.getGrantType());
        map.add("client_id", kakaoProperties.getClientId());
        map.add("redirect_uri", kakaoProperties.getRedirectUri());
        map.add("code", code);

        return restClient.post()
                .uri(kakaoProperties.getTokenRequestUri())
                .contentType(CONTENT_TYPE)
                .body(map)
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();
    }

    //카카오 엑세스 토큰을 이용한 유저정보 가져오기
    public UserJoinRequest getKakaoProfile(KakaoTokenResponse tokenResponse) {
        KakaoProfile kakaoProfile = restClient.post()
                .uri(kakaoProperties.getUserRequestUri() + "[\"kakao_account.profile\"]")
                .contentType(CONTENT_TYPE)
                .header("Authorization", "Bearer " + tokenResponse.accessToken())
                .retrieve()
                .toEntity(KakaoProfile.class)
                .getBody();

        return new UserJoinRequest(kakaoProfile, kakaoProperties.getPassword());
    }

}
