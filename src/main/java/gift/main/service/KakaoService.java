package gift.main.service;

import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoProfile;
import gift.main.dto.KakaoToken;
import gift.main.dto.KakaoUser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static io.jsonwebtoken.lang.Strings.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class KakaoService {

    private static final MediaType CONTENT_TYPE = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        restClient = RestClient.create();
    }

    //카카오 인가코드를 이용한 엑세스 토큰 요청하기
    public KakaoToken requestKakaoToken(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", kakaoProperties.grantType());
        map.add("client_id", kakaoProperties.clientId());
        map.add("redirect_uri", kakaoProperties.redirectUri());
        map.add("code", code);

        return restClient.post()
                .uri(kakaoProperties.tokenRequestUri())
                .contentType(CONTENT_TYPE)
                .body(map)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();
    }

    //카카오 엑세스 토큰을 이용한 유저정보 가져오기
    public KakaoUser getKakaoProfile(KakaoToken tokenResponse) {
        KakaoProfile kakaoProfile = restClient.post()
                .uri(kakaoProperties.userRequestUri() + "[\"kakao_account.profile\"]")
                .contentType(CONTENT_TYPE)
                .header(AUTHORIZATION, BEARER + tokenResponse.accessToken())
                .retrieve()
                .toEntity(KakaoProfile.class)
                .getBody();

        return new KakaoUser(kakaoProfile, kakaoProperties.password());

    }

}
