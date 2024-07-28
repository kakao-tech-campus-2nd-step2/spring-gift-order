package gift.domain.service;

import gift.domain.dto.response.KakaoUserInfoResponse;
import gift.domain.dto.response.OauthTokenResponse;
import gift.domain.exception.unauthorized.TokenUnexpectedErrorException;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OauthService {

    @Value("${oauth.kakao.api_key}")
    private String KAKAO_API_KEY;

    @Value("${oauth.kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URI;

    private final RestClient restClient;

    public OauthService() {
        this.restClient = RestClient.builder().build();
    }

    public OauthTokenResponse getOauthToken(Type userType, String authorizationCode) {
        if (Objects.requireNonNull(userType) == Type.KAKAO) {
            return getKakaoOauthToken(authorizationCode);
        }
        throw new IllegalStateException();
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String kakaoUserAccessToken) {
        try {
            return restClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + kakaoUserAccessToken)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class)
                .getBody();
        } catch (Exception e) {
            //TODO: 200 응답이 아닐 때를 조금 더 세분화시키기 (https://github.com/kakao-tech-campus-2nd-step2/spring-gift-order/pull/267#discussion_r1692966327)
            throw new TokenUnexpectedErrorException();
        }
    }

    private OauthTokenResponse getKakaoOauthToken(String authorizationCode) {
        try {
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", KAKAO_API_KEY);
            body.add("redirect_uri", KAKAO_REDIRECT_URI);
            body.add("code", authorizationCode);

            return restClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(OauthTokenResponse.class)
                .getBody();

        } catch (Exception e) {
            //TODO: 200 응답이 아닐 때를 조금 더 세분화시키기 (https://github.com/kakao-tech-campus-2nd-step2/spring-gift-order/pull/267#discussion_r1692966327)
            throw new TokenUnexpectedErrorException();
        }
    }
}
