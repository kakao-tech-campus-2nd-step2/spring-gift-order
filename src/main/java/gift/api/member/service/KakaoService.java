package gift.api.member.service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import gift.api.member.dto.TokenResponse;
import gift.api.member.dto.UserInfoResponse;
import gift.global.config.KakaoProperties;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final MemberService memberService;
    private final KakaoProperties properties;
    private final RestClient restClient;

    public KakaoService(MemberService memberService, KakaoProperties properties) {
        this.memberService = memberService;
        this.properties = properties;
        restClient = RestClient.create();
    }

    public void login(String code) {
        ResponseEntity<TokenResponse> tokenResponse = obtainToken(code);
        ResponseEntity<UserInfoResponse> userInfoResponse = obtainUserInfo(tokenResponse);
        memberService.loginKakao(userInfoResponse.getBody().kakaoAccount());
        memberService.saveKakaoToken(userInfoResponse.getBody().kakaoAccount().email(),
            tokenResponse.getBody().accessToken());
    }

    private ResponseEntity<TokenResponse> obtainToken(String code) {
        return restClient.post()
            .uri(URI.create(properties.url().token()))
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(createBody(code))
            .retrieve()
            .toEntity(TokenResponse.class);
    }

    private LinkedMultiValueMap<Object, Object> createBody(String code) {
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.url().redirect());
        body.add("code", code);
        return body;
    }

    private ResponseEntity<UserInfoResponse> obtainUserInfo(
        ResponseEntity<TokenResponse> tokenResponse) {
        return restClient.get()
            .uri(properties.url().user(), uriBuilder -> uriBuilder
                .queryParam("property_keys", "[\"kakao_account.email\"]")
                .build())
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .header(AUTHORIZATION, "Bearer " + tokenResponse.getBody().accessToken())
            .retrieve()
            .toEntity(UserInfoResponse.class);
    }
}
